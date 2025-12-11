package com.dsmovil.studiobarber.ui.screens.client.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.data.local.SessionManager
import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationRequest
import com.dsmovil.studiobarber.domain.usecases.home.AddReservationUseCase
import com.dsmovil.studiobarber.domain.usecases.timeblock.VerifyAvailabilityUseCase
import com.dsmovil.studiobarber.ui.components.client.selector.HourItem
import com.dsmovil.studiobarber.utils.convertTo24HourFormat
import com.dsmovil.studiobarber.utils.formatTimeForDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ClientCalendarViewModel @Inject constructor(
    private val addReservationUseCase: AddReservationUseCase,
    private val verifyAvailabilityUseCase: VerifyAvailabilityUseCase,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientCalendarUiState())
    val uiState: StateFlow<ClientCalendarUiState> = _uiState

    private val serviceId: Long = savedStateHandle["serviceId"]!!
    private val barberId: Long = savedStateHandle["barberId"]!!

    init {
        loadUserInfo()
        loadCalendarDays()
        fetchAvailabilityForCurrentState()
    }

    private fun loadUserInfo() {
        val currentUsername = sessionManager.getCurrentUsername()

        if (currentUsername != null) {
            _uiState.update { it.copy(userName = currentUsername) }
        }
    }

    private fun loadCalendarDays() {
        val today = LocalDate.now()
        val daysList = mutableListOf<DayItem>()
        val locale = Locale.Builder().setLanguage("es").setRegion("ES").build()

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
                selectedMonth = currentMonth ,
                selectedHour = null,
                selectedDate = today
            )
        }
    }

    fun onVisibleDayChanged(dayItem: DayItem) {
        if (_uiState.value.selectedMonth != dayItem.fullMonthName) {
            _uiState.update { it.copy(selectedMonth = dayItem.fullMonthName) }
        }
    }

    fun selectDate(dayItem: DayItem) {
        _uiState.update { it.copy(selectedDate = dayItem.date, selectedHour = null) }
        fetchAvailabilityForCurrentState()
    }

    fun selectHour(hour: String) {
        _uiState.update { it.copy(selectedHour = hour) }
    }

    fun toggleAmPm() {
        val newIsAmState = !_uiState.value.isAm
        _uiState.update {
            it.copy(
                isAm = newIsAmState,
                selectedHour = null
            )
        }
        fetchAvailabilityForCurrentState()
    }

    private fun fetchAvailabilityForCurrentState() {
        val selectedDate = _uiState.value.selectedDate ?: return
        val isAm = _uiState.value.isAm

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val baseHours = if (isAm) {
                listOf("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30")
            } else {
                listOf("14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30")
            }
            val hourItemsDeferred = baseHours.map { timeString ->
                async {
                    val startTime = LocalTime.parse(timeString)
                    val endTime = startTime.plusMinutes(30)

                    val result = verifyAvailabilityUseCase(
                        barberId = barberId,
                        date = selectedDate,
                        startTime = startTime,
                        endTime = endTime
                    )

                    val isAvailable = result.getOrDefault(false)

                    val displayTime = formatTimeForDisplay(startTime, false)

                    HourItem(displayTime, isAvailable)
                }
            }

            val hourItems = hourItemsDeferred.awaitAll()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    hours = hourItems
                )
            }
        }
    }

    fun reserve(onSuccess: () -> Unit, onError: (String) -> Unit) {

        val date = _uiState.value.selectedDate
        val hour12 = _uiState.value.selectedHour
        val isAm = _uiState.value.isAm

        if (date == null || hour12 == null) {
            onError("Por favor selecciona una fecha y una hora.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentUserId = sessionManager.getCurrentUserId()

            if (currentUserId != null) {
                val reservationRequest = ReservationRequest(
                    serviceId = serviceId,
                    userId = currentUserId,
                    barberId = barberId,
                    date = date.toString(),
                    timeStart = convertTo24HourFormat(hour12, isAm)
                )

                val result = addReservationUseCase(reservationRequest)

                _uiState.update { it.copy(isLoading = false) }

                if (result.isSuccess) {
                    onSuccess()
                } else {
                    val msg = result.exceptionOrNull()?.message ?: "Error desconocido al reservar"
                    onError(msg)
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
                onError("No se encontró la sesión del usuario. Por favor inicia sesión nuevamente.")
            }
        }
    }
}
