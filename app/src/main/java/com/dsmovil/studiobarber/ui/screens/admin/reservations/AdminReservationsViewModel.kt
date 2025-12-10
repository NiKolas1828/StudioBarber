package com.dsmovil.studiobarber.ui.screens.admin.reservations

import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.home.DeleteReservationsUseCase
import com.dsmovil.studiobarber.domain.usecases.home.GetAllReservationsUseCase
import com.dsmovil.studiobarber.ui.screens.client.calendar.DayItem
import com.dsmovil.studiobarber.ui.screens.utils.BaseViewModel
import com.dsmovil.studiobarber.ui.screens.utils.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AdminReservationsViewModel @Inject constructor(
    private val getReservationsUseCase: GetAllReservationsUseCase,
    private val deleteReservationsUseCase: DeleteReservationsUseCase,
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase) {

    private val _uiState = MutableStateFlow(AdminReservationsUiState())
    val uiState: StateFlow<AdminReservationsUiState> = _uiState.asStateFlow()

    private val _messageChannel = Channel<UiMessage>()
    val messageChannel = _messageChannel.receiveAsFlow()

    private var allReservationsCache: List<Reservation> = emptyList()

    init {
        loadCalendarDays()
        loadReservations()
    }

    // --- Lógica de Calendario ---

    fun selectDate(dayItem: DayItem) {
        _uiState.update {
            it.copy(selectedDate = dayItem.date)
        }
        applyFilters()
    }

    fun onVisibleDayChanged(dayItem: DayItem) {
        if (_uiState.value.selectedMonth != dayItem.fullMonthName) {
            _uiState.update {
                it.copy(selectedMonth = dayItem.fullMonthName)
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

    // --- Lógica de Reservas ---

    private fun loadReservations(){
        viewModelScope.launch {
            _uiState.update { it.copy(reservationState = AdminReservationsUiState.ReservationDataState.Loading) }

            val result = getReservationsUseCase()

            result.fold(
                onSuccess = { reservationsList ->
                    allReservationsCache = reservationsList
                    applyFilters()
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            reservationState = AdminReservationsUiState.ReservationDataState.Error(
                                message = exception.message ?: "Error desconocido al cargar reservas"
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
            reservation.date.isEqual(selectedDate)
        }

        _uiState.update {
            it.copy(
                reservationState = AdminReservationsUiState.ReservationDataState.Success(reservations = filteredList)
            )
        }
    }

    fun deleteReservation(reservationId: Long) {
        viewModelScope.launch {
            val result = deleteReservationsUseCase(reservationId)

            if(result.isSuccess) {

                allReservationsCache = allReservationsCache.filter { it.id != reservationId }
                applyFilters()

                showMessage("Reserva eliminada")
            } else {
                showMessage("Error: No se pudo eliminar la reserva", isError = true)
            }
        }
    }

    private fun showMessage(message: String, isError: Boolean = false) {
        viewModelScope.launch {
            _messageChannel.send(UiMessage(message, isError))
        }
    }
}
