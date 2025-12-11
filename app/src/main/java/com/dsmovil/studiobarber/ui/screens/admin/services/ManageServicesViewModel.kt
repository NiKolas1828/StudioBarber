package com.dsmovil.studiobarber.ui.screens.admin.services

import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.AddServiceUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.DeleteServiceUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.GetServicesUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.UpdateServiceUseCase
import com.dsmovil.studiobarber.ui.screens.utils.BaseViewModel
import com.dsmovil.studiobarber.ui.screens.utils.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageServicesViewModel @Inject constructor(
    private val getServicesUseCase: GetServicesUseCase,
    private val addServiceUseCase: AddServiceUseCase,
    private val updateServiceUseCase: UpdateServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase,
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase) {
    // Estados de la UI
    private val _uiState = MutableStateFlow<ManageServicesUiState>(ManageServicesUiState.Loading)
    val uiState: StateFlow<ManageServicesUiState> = _uiState.asStateFlow()

    // Estados del dialogo
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService: StateFlow<Service?> = _selectedService.asStateFlow()

    // Estados del snackbar
    private val _messageChannel = Channel<UiMessage>()
    val messageChannel = _messageChannel.receiveAsFlow()

    init {
        loadServices()
    }

    private fun showMessage(message: String, isError: Boolean = false) {
        viewModelScope.launch {
            _messageChannel.send(UiMessage(message, isError))
        }
    }

    private fun validateInput(name: String, description: String, priceStr: String): List<String> {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("• Falta el nombre")
        }

        if (description.isBlank()) {
            errors.add("• Falta la descripción")
        }

        val price = priceStr.toDoubleOrNull()
        if (price == null) {
            errors.add("• El precio debe ser un numero válido")
        } else if (price <= 0) {
            errors.add("• El precio debe ser mayor que 0")
        }

        return errors
    }

    private fun saveService(name: String, description: String, price: Double) {
        viewModelScope.launch {
            _showDialog.value = false
            val currentService = _selectedService.value

            val result = if (currentService == null) {
                val newService = Service(
                    id = System.currentTimeMillis(),
                    name = name,
                    description = description,
                    price = price
                )

                addServiceUseCase(newService)
            } else {
                val updatedService = currentService.copy(
                    name = name,
                    description = description,
                    price = price
                )

                updateServiceUseCase(updatedService)
            }

            if (result.isSuccess) {
                loadServices()
                showMessage(if (currentService == null) "Servicio creado" else "Servicio actualizado")
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                showMessage(errorMsg, isError = true)
            }

            _selectedService.value = null
        }
    }

    fun loadServices() {
        viewModelScope.launch {
            _uiState.value = ManageServicesUiState.Loading

            val result = getServicesUseCase()

            result.fold(
                onSuccess = { servicesList ->
                    _uiState.value = ManageServicesUiState.Success(services = servicesList)
                },
                onFailure = { exception ->
                    _uiState.value = ManageServicesUiState.Error(
                        message = exception.message ?: "Error desconocido al cargar servicios"
                    )
                }
            )
        }
    }

    fun openAddDialog() {
        _selectedService.value = null
        _showDialog.value = true
    }

    fun openEditDialog(service: Service) {
        _selectedService.value = service
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
        _selectedService.value = null
    }

    fun onConfirmDialog(name: String, description: String, priceStr: String) {
        val validationErrors = validateInput(name, description, priceStr)

        if (validationErrors.isNotEmpty()) {
            val finalMessage = validationErrors.joinToString("\n")
            showMessage(finalMessage, true)
            _showDialog.value = false

            return
        }

        saveService(name, description, priceStr.toDouble())
    }

    fun deleteService(serviceId: Long) {
        viewModelScope.launch {
            val result = deleteServiceUseCase(serviceId)

            if (result.isSuccess) {
                loadServices()
                showMessage("Servicio eliminado")
            } else {
                showMessage("Error: No se pudo eliminar el servicio", true)
            }
        }
    }

    fun clearError() {
        loadServices()
    }
}