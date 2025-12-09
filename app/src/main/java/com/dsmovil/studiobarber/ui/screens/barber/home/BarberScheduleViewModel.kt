package com.dsmovil.studiobarber.ui.screens.barber.home

import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.home.GetReservationsUseCase
import com.dsmovil.studiobarber.ui.screens.client.calendar.DayItem
import com.dsmovil.studiobarber.ui.screens.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BarberScheduleViewModel @Inject constructor(
    private val getReservationsUseCase: GetReservationsUseCase,
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase) {

    private val _uiState = MutableStateFlow(BarberScheduleUiState())
    val uiState: StateFlow<BarberScheduleUiState> = _uiState

    private var allReservationsCache: List<Reservation> = emptyList()

    init {
        loadCalendarDays()
        loadReservations()
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        applyFilters()
    }

    fun onVisibleDayChanged(dayItem: DayItem) {
        if (_uiState.value.selectedMonth != dayItem.fullMonthName) {
            _uiState.update { it.copy(selectedMonth = dayItem.fullMonthName) }
        }
    }

    private fun loadReservations() {
        viewModelScope.launch {
            _uiState.update { it.copy(reservationState = BarberScheduleUiState.ReservationDataState.Loading) }

            val result = getReservationsUseCase()

            result.fold(
                onSuccess = { reservationsList ->
                    allReservationsCache = reservationsList
                    applyFilters()
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            reservationState = BarberScheduleUiState.ReservationDataState.Error(
                                message = exception.message ?: "Error al cargar reservas"
                            )
                        )
                    }
                }
            )
        }
    }

    private fun applyFilters() {
        val selectedDate = _uiState.value.selectedDate

        val filteredList = allReservationsCache.filter { reservation ->
            val isSameDate = reservation.date.isEqual(selectedDate)
            val isBarberJuan = reservation.nameBarber.contains("Juan", ignoreCase = true)

            isSameDate && isBarberJuan
        }

        _uiState.update {
            it.copy(
                reservationState = BarberScheduleUiState.ReservationDataState.Success(filteredList)
            )
        }
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

            daysList.add(DayItem(date, date.dayOfMonth.toString(), dayName, monthName))
        }

        val currentMonth = today.month.getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

        _uiState.update { it.copy(days = daysList, selectedMonth = currentMonth) }
    }
}
