package com.dsmovil.studiobarber.ui.screens.admin.reservations

import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.ui.screens.client.calendar.DayItem
import java.time.LocalDate

data class AdminReservationsUiState(
    val username: String = "Usuario",
    val reservationState: ReservationDataState = ReservationDataState.Loading,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedMonth: String = "",
    val days: List<DayItem> = emptyList()
) {
    sealed class ReservationDataState {
        data object Loading : ReservationDataState()
        data class Success(val reservations: List<Reservation>) : ReservationDataState()
        data class Error(val message: String) : ReservationDataState()
    }
}
