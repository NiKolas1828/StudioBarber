package com.dsmovil.studiobarber.ui.screens.client.home

import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.GetBarbersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.dsmovil.studiobarber.ui.screens.admin.BaseAdminViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ClientHomeViewModel @Inject constructor(
    private val getBarberUseCase: GetBarbersUseCase,
    logoutUseCase: LogoutUseCase
) : BaseAdminViewModel(logoutUseCase){

    private val _uiState = MutableStateFlow(ClientHomeUiState())
    val uiState: StateFlow<ClientHomeUiState> = _uiState.asStateFlow()

    init {
        loadBarbers()
        loadServices()
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

            val fake = listOf(
                Service(1, "Corte de cabello", "Corte profesional"),
                Service(2, "Barba", "Arreglo de barba"),
                Service(3, "Cejas", "Perfilado de cejas")
            )

            _uiState.update {
                it.copy(servicesState = ClientHomeUiState.ServicesDataState.Success(fake))
            }
        }
    }

    fun changeOption(option: String){
        _uiState.value = _uiState.value.copy(selectedOption = option)
    }

    fun selectBarber(id: Long) {
        _uiState.value = _uiState.value.copy(selectedBarberId = id)
    }

    fun selectService(id: Long) {
        _uiState.value = _uiState.value.copy(selectedServiceId = id)
    }

}
