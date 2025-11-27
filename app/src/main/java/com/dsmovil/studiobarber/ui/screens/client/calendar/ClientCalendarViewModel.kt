package com.dsmovil.studiobarber.ui.screens.client.calendar

import android.util.Log
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

        val currentMonth = today.month.getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

        _uiState.update {
            it.copy(
                days = daysList,
                selectedMonth = currentMonth
            )
        }
    }

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
        val newIsAmState = !_uiState.value.isAm
        _uiState.update {
            it.copy(
                isAm = newIsAmState,
                hours = getHoursForState(newIsAmState),
                selectedHour = null
            )
        }
    }

    private fun loadHours() {
        _uiState.update { it.copy(hours = getHoursForState(true)) } // Iniciamos con AM
    }
    private fun getHoursForState(isAm: Boolean): List<HourItem> {
        return if (isAm) {
            // Horas de la MAÑANA (AM)
            listOf(
                HourItem("8:00", true), HourItem("8:30", false),
                HourItem("9:00", true), HourItem("9:30", false),
                HourItem("10:00", true), HourItem("10:30", false),
                HourItem("11:00", true), HourItem("11:30", false)
            )
        } else {
            listOf(
                HourItem("2:00", true), HourItem("2:30", false),
                HourItem("3:00", true), HourItem("3:30", false),
                HourItem("4:00", true), HourItem("4:30", false),
                HourItem("5:00", true), HourItem("5:30", false)
            )
        }
    }

    fun reserve() {
        val date = _uiState.value.selectedDate
        val hour = _uiState.value.selectedHour
        if (date != null && hour != null) {
            Log.d("ClientReservationVM","Reservando el $date a las $hour")
        }
    }
}
