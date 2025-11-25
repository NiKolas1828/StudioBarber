package com.dsmovil.studiobarber.ui.screens.client.reservations

import com.dsmovil.studiobarber.domain.models.Reservation

data class ClientReservationUiState(
    val userName: String = "Usuario",
    val reservationState: ReservationDataState = ReservationDataState.Loading
) {
    sealed class ReservationDataState {
        data object Loading : ReservationDataState()
        data class Success(
            val reservations: List<Reservation>
        ) : ReservationDataState()
        data class Error(
            val message: String
        ) : ReservationDataState()
    }
}