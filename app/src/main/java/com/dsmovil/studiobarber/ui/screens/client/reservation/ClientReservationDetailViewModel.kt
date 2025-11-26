package com.dsmovil.studiobarber.ui.screens.client.reservation

import androidx.lifecycle.ViewModel
import com.dsmovil.studiobarber.ui.components.client.selector.DayItem
import com.dsmovil.studiobarber.ui.components.client.selector.HourItem

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ClientReservationDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ClientReservationUiState())
    val uiState: StateFlow<ClientReservationUiState> = _uiState

    init {
        loadDays()
        loadHours()
    }

    private fun loadDays() {
        val fakeDays = listOf(
            DayItem(13, "Dom"),
            DayItem(14, "Lun"),
            DayItem(15, "Mar"),
            DayItem(16, "Mié"),
            DayItem(17, "Jue"),
            DayItem(18, "Vie"),
            DayItem(19, "Sáb")
        )

        _uiState.update { it.copy(days = fakeDays) }
    }

    private fun loadHours() {
        val fakeHours = listOf(
            HourItem("8:00", true),
            HourItem("8:30", false),
            HourItem("9:00", true),
            HourItem("9:30", false),
            HourItem("10:00", true),
            HourItem("10:30", true),
            HourItem("11:00", true),
            HourItem("11:30", false)
        )

        _uiState.update { it.copy(hours = fakeHours) }
    }

    fun selectDay(day: Int) {
        _uiState.update { it.copy(selectedDay = day) }
    }

    fun selectHour(hour: String) {
        _uiState.update { it.copy(selectedHour = hour) }
    }

    fun toggleAmPm() {
        _uiState.update { it.copy(isAm = !it.isAm) }
    }

    fun reserve() {
        // Implementar la reserva
    }
}
