package com.dsmovil.studiobarber.ui.screens.admin.barbers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.usecases.DeleteBarberUseCase
import com.dsmovil.studiobarber.domain.usecases.GetBarbersUseCase
import com.dsmovil.studiobarber.domain.usecases.UpdateBarberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageBarbersViewModel @Inject constructor(
    private val getBarbersUseCase: GetBarbersUseCase,
    private val deleteBarberUseCase: DeleteBarberUseCase,
    private val updateBarberUseCase: UpdateBarberUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ManageBarbersUiState>(ManageBarbersUiState.Loading)
    val uiState: StateFlow<ManageBarbersUiState> = _uiState.asStateFlow()

    init {
        loadBarbers()
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

    fun deleteBarber(barberId: Long) {
        viewModelScope.launch {
            val result = deleteBarberUseCase(barberId)

            if (result.isSuccess) {
                loadBarbers()
            } else {
                _uiState.value = ManageBarbersUiState.Error(
                    message = result.exceptionOrNull()?.message ?: "No se pudo eliminar el barbero"
                )
            }
        }
    }

    fun onEditClick(barberId: Long) {
        val currentState = _uiState.value

        if (currentState is ManageBarbersUiState.Success) {
            _uiState.value = currentState.copy(editingBarberId = barberId)
        }
    }

    fun onCancelEdit() {
        val currentState = _uiState.value

        if (currentState is ManageBarbersUiState.Success) {
            _uiState.value = currentState.copy(editingBarberId = null)
        }
    }

    fun onSaveEdit(barber: Barber, newName: String) {
        viewModelScope.launch {
            val updatedBarber = barber.copy(name = newName)

            val result = updateBarberUseCase(updatedBarber)

            if (result.isSuccess) {
                loadBarbers()

            } else {
                _uiState.value = ManageBarbersUiState.Error(
                    message = result.exceptionOrNull()?.message ?: "No se pudo actualizar el barbero"
                )
            }
        }
    }

    fun clearError() {
        loadBarbers()
    }
}