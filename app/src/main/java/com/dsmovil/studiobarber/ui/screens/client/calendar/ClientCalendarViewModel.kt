package com.dsmovil.studiobarber.ui.screens.client.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.data.local.SessionManager
import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationRequest
import com.dsmovil.studiobarber.domain.usecases.home.AddReservationUseCase
import com.dsmovil.studiobarber.domain.usecases.home.EditReservationUseCase
import com.dsmovil.studiobarber.ui.components.client.selector.HourItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ClientCalendarViewModel @Inject constructor(
    private val addReservationUseCase: AddReservationUseCase,
    private val editReservationUseCase: EditReservationUseCase,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientCalendarUiState())
    val uiState: StateFlow<ClientCalendarUiState> = _uiState

    private val serviceId: Long = checkNotNull(savedStateHandle["serviceId"])
    private val barberId: Long = checkNotNull(savedStateHandle["barberId"])

    private val reservationIdArg: Long = savedStateHandle.get<Long>("reservationId") ?: -1L

    private val isEditMode = reservationIdArg != -1L

    private val reservationId: Long? = if (isEditMode) reservationIdArg else null

    init {
        val reservationId = savedStateHandle.get<Long>("reservationId")
        _uiState.update { it.copy(isEditMode = isEditMode) }

        loadCalendarDays()
        loadHours()

        val prevDate = savedStateHandle.get<String>("prevDate")
        val prevTime = savedStateHandle.get<String>("prevTime")

        if (prevDate != null && prevTime != null) {
            setupPreSelectedData(prevDate, prevTime)
        }
    }

    private fun setupPreSelectedData(dateStr: String, timeStr: String) {
        try {
            val date = LocalDate.parse(dateStr)

            val time = LocalTime.parse(timeStr)
            val isAm = time.hour < 12

            val formatter = DateTimeFormatter.ofPattern("h:mm")
            val hour12Str = time.format(formatter)

            _uiState.update {
                it.copy(
                    selectedDate = date,
                    selectedHour = hour12Str,
                    isAm = isAm,
                    hours = getHoursForState(isAm)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    fun confirmAction(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (isEditMode) {
            updateReservation(onSuccess, onError)
        } else {
            createReservation(onSuccess, onError)
        }
    }

    private fun createReservation(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (!validateInput(onError)) return@launch

            val request = buildReservationRequest() ?: return@launch

            setLoading(true)
            val result = addReservationUseCase(request)
            setLoading(false)

            handleResult(result, onSuccess, onError)
        }
    }

    private fun updateReservation(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (!validateInput(onError)) return@launch

            val request = buildReservationRequest() ?: return@launch

            if (reservationId == null) {
                onError("Error: ID de reserva no válido")
                return@launch
            }

            setLoading(true)
            val result = editReservationUseCase(reservationId, request)
            setLoading(false)

            handleResult(result, onSuccess, onError)
        }
    }

    private fun buildReservationRequest(): ReservationRequest? {
        val currentState = _uiState.value
        val currentUserId = sessionManager.getCurrentUserId()

        if (currentUserId == null) return null

        return ReservationRequest(
            serviceId = serviceId,
            userId = currentUserId,
            barberId = barberId,
            date = currentState.selectedDate.toString(),
            timeStart = convertTo24HourFormat(currentState.selectedHour!!, currentState.isAm)
        )
    }

    private fun validateInput(onError: (String) -> Unit): Boolean {
        val state = _uiState.value

        if (state.selectedDate == null || state.selectedHour == null) {
            onError("Por favor selecciona una fecha y una hora.")
            return false
        }

        if (sessionManager.getCurrentUserId() == null) {
            onError("No se encontró la sesión del usuario. Por favor inicia sesión nuevamente.")
            return false
        }
        return true
    }

    private fun handleResult(result: Result<Any>, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (result.isSuccess) {
            onSuccess()
        } else {
            val msg = result.exceptionOrNull()?.message ?: "Ocurrió un error inesperado"
            onError(msg)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun convertTo24HourFormat(hour12: String, isAm: Boolean): String {
        try {
            val parts = hour12.split(":")
            var hour = parts[0].toInt()
            val minute = parts[1]

            if (isAm) {
                if (hour == 12) hour = 0
            } else {
                if (hour != 12) hour += 12
            }
            return String.format(Locale.getDefault(), "%02d:%s", hour, minute)
        } catch (e: Exception) {
            return "00:00" // Fallback
        }
    }

    private fun loadCalendarDays() {
        val today = LocalDate.now()
        val daysList = mutableListOf<DayItem>()
        val locale = Locale("es", "ES")

        for (i in 0 until 365) {
            val date = today.plusDays(i.toLong())

            val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }.replace(".", "")
            val monthName = date.month.getDisplayName(TextStyle.FULL, locale)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

            daysList.add(DayItem(date, date.dayOfMonth.toString(), dayName, monthName))
        }

        val currentMonth = today.month.getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

        _uiState.update { it.copy(days = daysList, selectedMonth = currentMonth) }
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
}