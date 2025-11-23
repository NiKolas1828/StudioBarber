package com.dsmovil.studiobarber.ui.screens.admin.barbers

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.usecases.admin.DeleteBarberUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.GetBarbersUseCase
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.AddBarberUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.UpdateBarberUseCase
import com.dsmovil.studiobarber.ui.screens.admin.BaseAdminViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageBarbersViewModel @Inject constructor(
    private val getBarbersUseCase: GetBarbersUseCase,
    private val deleteBarberUseCase: DeleteBarberUseCase,
    private val updateBarberUseCase: UpdateBarberUseCase,
    private val addBarberUseCase: AddBarberUseCase,
    logoutUseCase: LogoutUseCase
) : BaseAdminViewModel(logoutUseCase) {
    // Estados de la UI
    private val _uiState = MutableStateFlow<ManageBarbersUiState>(ManageBarbersUiState.Loading)
    val uiState: StateFlow<ManageBarbersUiState> = _uiState.asStateFlow()

    // Estados del dialogo
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _selectedBarber = MutableStateFlow<Barber?>(null)
    val selectedBarber: StateFlow<Barber?> = _selectedBarber.asStateFlow()

    // Estados del snackBar
    private val _messageChannel = Channel<UiMessage>()
    val messageChannel = _messageChannel.receiveAsFlow()

    init {
        loadBarbers()
    }

    private fun showMessage(message: String, isError: Boolean = false) {
        viewModelScope.launch {
            _messageChannel.send(UiMessage(message, isError))
        }
    }

    private fun validateInput(name: String, email: String, phone: String, password: String): List<String> {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("• Falta el nombre")
        }

        if (email.isBlank()) {
            errors.add("• Falta el correo")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add("• Correo inválido")
        }

        if (phone.isBlank()) {
            errors.add("• Falta el teléfono")
        } else if (phone.length != 10) {
            errors.add("• Teléfono inválido")
        }

        val isCreating = _selectedBarber.value == null
        if (isCreating && password.length < 8) {
            errors.add("• La contraseña debe ser almenos de 8 caracteres")
        }

        return errors
    }

    private fun saveBarber(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            _showDialog.value = false
            val currentBarber = _selectedBarber.value

            val result = if (currentBarber == null) {
                val newBarber = Barber(
                    id = System.currentTimeMillis(),
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    isActive = true
                )

                addBarberUseCase(newBarber)
            } else {
                val finalPassword = password.ifBlank { currentBarber.password }

                val updatedBarber = currentBarber.copy(
                    name = name,
                    email = email,
                    phone = phone,
                    password = finalPassword
                )

                updateBarberUseCase(updatedBarber)
            }

            if (result.isSuccess) {
                loadBarbers()
                showMessage(if (currentBarber == null) "Barbero creado" else "Barbero actualizado")
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                showMessage("Error: $errorMsg", isError = true)
            }

            _selectedBarber.value = null
        }
    }

    fun loadBarbers() {
        viewModelScope.launch {
            _uiState.value = ManageBarbersUiState.Loading

            val result = getBarbersUseCase()

            result.fold(
                onSuccess = { barbersList ->
                    _uiState.value = ManageBarbersUiState.Success(barbers = barbersList)
                },
                onFailure = { exception ->
                    _uiState.value = ManageBarbersUiState.Error(
                        message = exception.message ?: "Error desconocido al cargar barberos"
                    )
                }
            )
        }
    }

    fun openAddDialog() {
        _selectedBarber.value = null
        _showDialog.value = true
    }

    fun openEditDialog(barber: Barber) {
        _selectedBarber.value = barber
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
        _selectedBarber.value = null
    }

    fun onConfirmDialog(name: String, email: String, phone: String, password: String) {
        val validationErrors = validateInput(name, email, phone, password)

        if (validationErrors.isNotEmpty()) {
            val finalMessage = validationErrors.joinToString("\n")
            showMessage(finalMessage, true)
            _showDialog.value = false

            return
        }

        saveBarber(name, email, phone, password)
    }

    fun deleteBarber(barberId: Long) {
        viewModelScope.launch {
            val result = deleteBarberUseCase(barberId)

            if (result.isSuccess) {
                loadBarbers()
                showMessage("Barbero eliminado")
            } else {
                showMessage("Error: No se pudo eliminar el barbero", true)
            }
        }
    }

    fun clearError() {
        loadBarbers()
    }
}