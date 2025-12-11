package com.dsmovil.studiobarber.ui.screens.client.home

import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.data.local.SessionManager
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.GetBarbersUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.GetServicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.dsmovil.studiobarber.ui.screens.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ClientHomeViewModel @Inject constructor(
    private val getBarberUseCase: GetBarbersUseCase,
    private val getServicesUseCase: GetServicesUseCase,
    private val sessionManager: SessionManager,
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase){

    private val _uiState = MutableStateFlow(ClientHomeUiState())
    val uiState: StateFlow<ClientHomeUiState> = _uiState.asStateFlow()

    init {
        loadUserInfo()
        loadBarbers()
        loadServices()
    }

    private fun loadUserInfo() {
        val currentUsername = sessionManager.getCurrentUsername()

        if (currentUsername != null) {
            _uiState.update { it.copy(userName = currentUsername) }
        }
    }

    private fun loadBarbers() {
        viewModelScope.launch {
            _uiState.update { it.copy (barbersState = ClientHomeUiState.BarbersDataState.Loading) }

            val result = getBarberUseCase()

            result.fold(
                onSuccess = { barbersList ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            barbersState = ClientHomeUiState.BarbersDataState.Success(barbers = barbersList)
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            barbersState = ClientHomeUiState.BarbersDataState.Error(
                                message = exception.message ?: "Error desconocido al cargar barberos"
                            )
                        )
                    }
                }
            )
        }
    }

    private fun loadServices() {
        viewModelScope.launch {
            _uiState.update { it.copy(servicesState = ClientHomeUiState.ServicesDataState.Loading) }

            val result = getServicesUseCase()

            result.fold(
                onSuccess = {servicesList ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            servicesState = ClientHomeUiState.ServicesDataState.Success(services = servicesList)
                        )
                    }
                },
                onFailure = {exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            servicesState = ClientHomeUiState.ServicesDataState.Error(message = exception.message ?: "Error desconocido al cargar servicios")
                        )
                    }
                }
            )
        }
    }

    fun changeOption(option: SelectOptions){
        _uiState.value = _uiState.value.copy(selectedOption = option)
    }

    fun selectBarber(id: Long) {
        if (_uiState.value.selectedBarberId == id) {
            _uiState.value = _uiState.value.copy(selectedBarberId = null)
            return
        }

        _uiState.value = _uiState.value.copy(selectedBarberId = id)
    }

    fun selectService(id: Long) {
        if (_uiState.value.selectedServiceId == id) {
            _uiState.value = _uiState.value.copy(selectedServiceId = null)
            return
        }

        _uiState.value = _uiState.value.copy(selectedServiceId = id)
    }

}
