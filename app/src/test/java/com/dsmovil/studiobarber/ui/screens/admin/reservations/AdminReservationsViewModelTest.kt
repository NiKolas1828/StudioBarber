package com.dsmovil.studiobarber.ui.screens.admin.reservations

import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.home.DeleteReservationsUseCase
import com.dsmovil.studiobarber.domain.usecases.home.GetAllReservationsUseCase
import com.dsmovil.studiobarber.ui.screens.client.calendar.DayItem
import com.dsmovil.studiobarber.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class AdminReservationsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getReservationsUseCase = mockk<GetAllReservationsUseCase>()
    private val deleteReservationsUseCase = mockk<DeleteReservationsUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)

    private lateinit var viewModel: AdminReservationsViewModel

    @Test
    fun `initialization loads days and reservations`() = runTest {
        val today = LocalDate.now()
        val reservation = mockk<Reservation>(relaxed = true) {
            coEvery { date } returns today
            coEvery { id } returns 1
        }
        val reservations = listOf(reservation)

        coEvery { getReservationsUseCase() } returns Result.success(reservations)

        viewModel = AdminReservationsViewModel(
            getReservationsUseCase,
            deleteReservationsUseCase,
            logoutUseCase,
        )

        val state = viewModel.uiState.value
        
        // Check days loaded
        assertTrue(state.days.isNotEmpty())
        assertEquals(365, state.days.size)
        
        // Check reservations loaded and filtered for today (default selection)
        assertTrue(state.reservationState is AdminReservationsUiState.ReservationDataState.Success)
        val loadedReservations = (state.reservationState as AdminReservationsUiState.ReservationDataState.Success).reservations
        assertEquals(1, loadedReservations.size)
        assertEquals(reservation, loadedReservations[0])
    }

    @Test
    fun `selectDate updates selected date and filters reservations`() = runTest {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        
        val resToday = mockk<Reservation>(relaxed = true) { 
            coEvery { date } returns today 
            coEvery { id } returns 1
        }
        val resTomorrow = mockk<Reservation>(relaxed = true) { 
            coEvery { date } returns tomorrow 
            coEvery { id } returns 2
        }
        
        coEvery { getReservationsUseCase() } returns Result.success(listOf(resToday, resTomorrow))

        viewModel = AdminReservationsViewModel(
            getReservationsUseCase,
            deleteReservationsUseCase,
            logoutUseCase
        )

        // Select tomorrow
        val tomorrowItem = DayItem(tomorrow, "1", "Mon", "January")
        viewModel.selectDate(tomorrowItem)

        val state = viewModel.uiState.value
        assertEquals(tomorrow, state.selectedDate)
        
        val loadedReservations = (state.reservationState as AdminReservationsUiState.ReservationDataState.Success).reservations
        assertEquals(1, loadedReservations.size)
        assertEquals(resTomorrow, loadedReservations[0])
    }

    @Test
    fun `deleteReservation success removes reservation from list`() = runTest {
        val today = LocalDate.now()
        val reservation = mockk<Reservation>(relaxed = true) {
            coEvery { date } returns today
            coEvery { id } returns 1
        }
        
        coEvery { getReservationsUseCase() } returns Result.success(listOf(reservation))
        coEvery { deleteReservationsUseCase(1) } returns Result.success(Unit)

        viewModel = AdminReservationsViewModel(
            getReservationsUseCase,
            deleteReservationsUseCase,
            logoutUseCase
        )

        viewModel.deleteReservation(1)

        coVerify { deleteReservationsUseCase(1) }
        
        val state = viewModel.uiState.value
        val loadedReservations = (state.reservationState as AdminReservationsUiState.ReservationDataState.Success).reservations
        assertTrue(loadedReservations.isEmpty())
        
        val message = viewModel.messageChannel.first()
        assertEquals("Reserva eliminada", message.message)
    }

    @Test
    fun `deleteReservation failure shows error message`() = runTest {
        val today = LocalDate.now()
        val reservation = mockk<Reservation>(relaxed = true) {
            coEvery { date } returns today
            coEvery { id } returns 1
        }
        
        coEvery { getReservationsUseCase() } returns Result.success(listOf(reservation))
        coEvery { deleteReservationsUseCase(1) } returns Result.failure(Exception("Error"))

        viewModel = AdminReservationsViewModel(
            getReservationsUseCase,
            deleteReservationsUseCase,
            logoutUseCase
        )

        viewModel.deleteReservation(1)

        val message = viewModel.messageChannel.first()
        assertTrue(message.isError)
        assertEquals("Error: No se pudo eliminar la reserva", message.message)
        
        // Reservation should still be there
        val state = viewModel.uiState.value
        val loadedReservations = (state.reservationState as AdminReservationsUiState.ReservationDataState.Success).reservations
        assertEquals(1, loadedReservations.size)
    }
}
