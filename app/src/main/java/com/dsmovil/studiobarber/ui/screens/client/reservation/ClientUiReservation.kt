package com.dsmovil.studiobarber.ui.screens.client.reservation

import com.dsmovil.studiobarber.ui.components.client.selector.DayItem
import com.dsmovil.studiobarber.ui.components.client.selector.HourItem

data class ClientReservationUiState(
    val userName: String = "Usuario",
    val days: List<DayItem> = emptyList(),
    val hours: List<HourItem> = emptyList(),
    val selectedDay: Int? = null,
    val selectedHour: String? = null,
    val isAm: Boolean = true
)
