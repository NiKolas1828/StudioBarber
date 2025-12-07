package com.dsmovil.studiobarber.ui.screens.barber.home

import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.ui.screens.client.calendar.DayItem
import java.time.LocalDate

data class BarberScheduleUiState(
    val title: String = "Mis Reservas",
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedMonth: String = "",
    val days: List<DayItem> = emptyList(),
    val reservationState: ReservationDataState = ReservationDataState.Loading
) {
    sealed interface ReservationDataState {
        data object Loading : ReservationDataState
        data class Success(val reservations: List<Reservation>) : ReservationDataState
        data class Error(val message: String) : ReservationDataState
    }
}