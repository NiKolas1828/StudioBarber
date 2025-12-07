package com.dsmovil.studiobarber.ui.screens.barber.home

import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.home.DeleteReservationsUseCase
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
    private val deleteReservationsUseCase: DeleteReservationsUseCase,
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase) {

    private val _uiState = MutableStateFlow(BarberScheduleUiState())
    val uiState: StateFlow<BarberScheduleUiState> = _uiState

    // Cache para guardar TODAS las reservas del repositorio y poder filtrar localmente
    private var allReservationsCache: List<Reservation> = emptyList()

    init {
        loadCalendarDays()
        loadReservations()
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        // Al cambiar la fecha, re-aplicamos el filtro sobre los datos que ya tenemos
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
                    // 1. Guardamos la lista completa en memoria
                    allReservationsCache = reservationsList

                    // 2. Aplicamos los filtros (Fecha + Nombre Juan)
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

    // Lógica central de filtrado
    private fun applyFilters() {
        val selectedDate = _uiState.value.selectedDate

        // Filtramos la cache
        val filteredList = allReservationsCache.filter { reservation ->
            val isSameDate = reservation.date.isEqual(selectedDate)
            // Filtramos por nombre de Barbero (ignora mayúsculas/minúsculas)
            val isBarberJuan = reservation.nameBarber.contains("Alexis", ignoreCase = true)

            isSameDate && isBarberJuan
        }

        // Actualizamos la UI con la lista filtrada
        _uiState.update {
            it.copy(
                reservationState = BarberScheduleUiState.ReservationDataState.Success(filteredList)
            )
        }
    }

    fun deleteReservation(id: Long) {
        viewModelScope.launch {
            val result = deleteReservationsUseCase(id)
            if (result.isSuccess) {
                loadReservations() // Recargar desde el repo para refrescar datos
            }
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