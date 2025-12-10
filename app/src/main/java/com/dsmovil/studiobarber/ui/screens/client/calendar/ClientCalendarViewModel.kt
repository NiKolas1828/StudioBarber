package com.dsmovil.studiobarber.ui.screens.client.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dsmovil.studiobarber.ui.components.client.selector.HourItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ClientCalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientCalendarUiState())
    val uiState: StateFlow<ClientCalendarUiState> = _uiState

    private val serviceId: Long = savedStateHandle["serviceId"]!!
    private val barberId: Long = savedStateHandle["barberId"]!!

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

    fun selectDate(dayItem: DayItem) {
        _uiState.update { it.copy(selectedDate = dayItem.date) }
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
        _uiState.update { it.copy(hours = getHoursForState(true)) }
    }
    private fun getHoursForState(isAm: Boolean): List<HourItem> {
        return if (isAm) {
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
        val hour12 = _uiState.value.selectedHour
        val isAm = _uiState.value.isAm

        if (date != null && hour12 != null) {

            val formattedDate = date.toString()
            val formattedTime24 = convertTo24HourFormat(hour12, isAm)
        }
    }

    private fun convertTo24HourFormat(hour12: String, isAm: Boolean): String {
        val parts = hour12.split(":")
        var hour = parts[0].toInt()
        val minute = parts[1]

        if (isAm) {
            if (hour == 12) hour = 0
        } else {
            if (hour != 12) hour += 12
        }

        return String.format(Locale.getDefault(), "%02d:%s", hour, minute)
    }
}
