package com.dsmovil.studiobarber.ui.screens.admin.reservations

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminReservationsViewModel @Inject constructor(
    private val getReservationsUseCase: GetReservationsUseCase,
    private val deleteReservationsUseCase: DeleteReservationsUseCase,
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase) {
    private val _uiState = MutableStateFlow<AdminReservationsUiState>(AdminReservationsUiState.Loading)
    val uiState: StateFlow<AdminReservationsUiState> = _uiState.asStateFlow()

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
            _uiState.value = AdminReservationsUiState.Loading

            val result = getReservationsUseCase()

            result.fold(
                onSuccess = { reservationsList ->
                   _uiState.value = AdminReservationsUiState.Success(reservations = reservationsList)
                },
                onFailure = { exception ->
                    _uiState.value = AdminReservationsUiState.Error(message = exception.message ?: "Error desconocido al cargar reservas")
                }
            )
        }
    }

    fun deleteReservation(reservationId: Long) {
        viewModelScope.launch {
            val result = deleteReservationsUseCase(reservationId)

            if(result.isSuccess) {
                loadReservations()
                showMessage("Reserva eliminada")
            } else {
                showMessage("Error: No se pudo eliminar la reserva")
            }
        }
    }
}