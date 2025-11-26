package com.dsmovil.studiobarber.ui.screens.client.reservation

import com.dsmovil.studiobarber.ui.components.client.selector.HourItem
import java.time.LocalDate

data class DayItem(
    val date: LocalDate,
    val dayNumber: String,
    val dayName: String,
    val fullMonthName: String
)

data class ClientReservationUiState(
    val userName: String = "Usuario",
    val days: List<DayItem> = emptyList(),
    val hours: List<HourItem> = emptyList(),
    val months: List<String> = emptyList(),
    val selectedMonth: String = "",
    val selectedDate: LocalDate? = null,
    val selectedHour: String? = null,
    val isAm: Boolean = true
)
