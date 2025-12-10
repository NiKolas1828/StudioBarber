package com.dsmovil.studiobarber.ui.screens.client.reservations

import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.home.DeleteReservationsUseCase
import com.dsmovil.studiobarber.domain.usecases.home.GetReservationsUseCase
import com.dsmovil.studiobarber.ui.screens.utils.BaseViewModel
import com.dsmovil.studiobarber.ui.screens.utils.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientReservationViewModel @Inject constructor(
    private val getReservationsUseCase: GetReservationsUseCase,
    private val deleteReservationsUseCase: DeleteReservationsUseCase,
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase){

    private val _uiState = MutableStateFlow(ClientReservationUiState())
    val uiState: StateFlow<ClientReservationUiState> = _uiState.asStateFlow()

    private val _messageChannel = Channel<UiMessage>()
    val messageChannel = _messageChannel.receiveAsFlow()

    init {
        loadReservations()
    }

    private fun showMessage(message: String, isError: Boolean = false) {
        viewModelScope.launch {
            _messageChannel.send(UiMessage(message, isError))
        }
    }

    private fun loadReservations(){
        viewModelScope.launch {
            _uiState.update { it.copy(reservationState = ClientReservationUiState.ReservationDataState.Loading) }

            val result = getReservationsUseCase(19)

            result.fold(
                onSuccess = { reservationsList ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            reservationState = ClientReservationUiState.ReservationDataState.Success(reservations = reservationsList)
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            reservationState = ClientReservationUiState.ReservationDataState.Error(
                                message = exception.message ?: "Error desconocido al cargar las reservas"
                            )
                        )
                    }
                }
            )
        }
    }

    fun deleteReservation(reservationId: Long) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(reservationState = ClientReservationUiState.ReservationDataState.Loading)
            }

            val result = deleteReservationsUseCase(reservationId)

            if (result.isSuccess) {
                loadReservations()

            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        reservationState = ClientReservationUiState.ReservationDataState.Error("No se pudo cancelar la reserva.")
                    )
                }
            }
        }
    }
}