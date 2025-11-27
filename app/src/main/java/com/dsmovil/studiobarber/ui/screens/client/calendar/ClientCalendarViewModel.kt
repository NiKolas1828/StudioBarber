package com.dsmovil.studiobarber.ui.screens.client.calendar

import androidx.lifecycle.ViewModel
import com.dsmovil.studiobarber.ui.components.client.selector.HourItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class ClientCalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ClientCalendarUiState())
    val uiState: StateFlow<ClientCalendarUiState> = _uiState

    init {
        loadCalendarDays()
        loadHours()
    }

    private fun loadCalendarDays() {
        val today = LocalDate.now()
        val daysList = mutableListOf<DayItem>()
        val locale = Locale("es", "ES")

        // Generamos los próximos 365 días (1 año de calendario continuo)
        for (i in 0 until 365) {
            val date = today.plusDays(i.toLong())

            val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
                .replace(".", "")

            val monthName = date.month.getDisplayName(TextStyle.FULL, locale)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

            daysList.add(
                DayItem(
                    date = date,
                    dayNumber = date.dayOfMonth.toString(),
                    dayName = dayName,
                    fullMonthName = monthName
                )
            )
        }

        // Inicializamos el estado con el mes actual
        val currentMonth = today.month.getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

        _uiState.update {
            it.copy(
                days = daysList,
                selectedMonth = currentMonth
            )
        }
    }

    // Esta función se llamará cuando el scroll de días cambie
    // para actualizar el título del mes arriba automáticamente
    fun onVisibleDayChanged(dayItem: DayItem) {
        if (_uiState.value.selectedMonth != dayItem.fullMonthName) {
            _uiState.update { it.copy(selectedMonth = dayItem.fullMonthName) }
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun selectHour(hour: String) {
        _uiState.update { it.copy(selectedHour = hour) }
    }

    fun toggleAmPm() {
        _uiState.update { it.copy(isAm = !it.isAm) }
    }

    private fun loadHours() {
        // Tu lógica de horas existente...
        val fakeHours = listOf(
            HourItem("8:00", true), HourItem("8:30", false),
            HourItem("9:00", true), HourItem("9:30", false),
            HourItem("10:00", true), HourItem("11:00", true)
        )
        _uiState.update { it.copy(hours = fakeHours) }
    }

    fun reserve() {
        val date = _uiState.value.selectedDate
        val hour = _uiState.value.selectedHour
        if (date != null && hour != null) {
            println("Reservando el $date a las $hour")
        }
    }
}
