package com.dsmovil.studiobarber.ui.screens.admin.barbers

import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.AddBarberUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.DeleteBarberUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.GetBarbersUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.UpdateBarberUseCase
import com.dsmovil.studiobarber.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ManageBarbersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getBarbersUseCase = mockk<GetBarbersUseCase>()
    private val deleteBarberUseCase = mockk<DeleteBarberUseCase>()
    private val updateBarberUseCase = mockk<UpdateBarberUseCase>()
    private val addBarberUseCase = mockk<AddBarberUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)

    private lateinit var viewModel: ManageBarbersViewModel

    @Test
    fun `loadBarbers success updates uiState to Success`() = runTest {
        val barbers = listOf(
            Barber(1, "Barber 1", "barber1@example.com", "1234567890", "pass", true),
            Barber(2, "Barber 2", "barber2@example.com", "0987654321", "pass", false)
        )
        coEvery { getBarbersUseCase() } returns Result.success(barbers)

        viewModel = ManageBarbersViewModel(
            getBarbersUseCase,
            deleteBarberUseCase,
            updateBarberUseCase,
            addBarberUseCase,
            logoutUseCase
        )

        val state = viewModel.uiState.value
        assertTrue(state is ManageBarbersUiState.Success)
        assertEquals(barbers, (state as ManageBarbersUiState.Success).barbers)
    }

    @Test
    fun `loadBarbers failure updates uiState to Error`() = runTest {
        val errorMessage = "Failed to load barbers"
        coEvery { getBarbersUseCase() } returns Result.failure(Exception(errorMessage))

        viewModel = ManageBarbersViewModel(
            getBarbersUseCase,
            deleteBarberUseCase,
            updateBarberUseCase,
            addBarberUseCase,
            logoutUseCase
        )

        val state = viewModel.uiState.value
        assertTrue(state is ManageBarbersUiState.Error)
        assertEquals(errorMessage, (state as ManageBarbersUiState.Error).message)
    }

    @Test
    fun `dialog state management works correctly`() = runTest {
        coEvery { getBarbersUseCase() } returns Result.success(emptyList())
        viewModel = ManageBarbersViewModel(
            getBarbersUseCase,
            deleteBarberUseCase,
            updateBarberUseCase,
            addBarberUseCase,
            logoutUseCase
        )

        viewModel.openAddDialog()
        assertTrue(viewModel.showDialog.value)
        assertNull(viewModel.selectedBarber.value)

        val barber = Barber(1, "Barber 1", "barber1@example.com", "1234567890", "pass", true)
        viewModel.openEditDialog(barber)
        assertTrue(viewModel.showDialog.value)
        assertEquals(barber, viewModel.selectedBarber.value)

        viewModel.closeDialog()
        assertFalse(viewModel.showDialog.value)
        assertNull(viewModel.selectedBarber.value)
    }

    @Test
    fun `onConfirmDialog validates input and shows error`() = runTest {
        coEvery { getBarbersUseCase() } returns Result.success(emptyList())
        viewModel = ManageBarbersViewModel(
            getBarbersUseCase,
            deleteBarberUseCase,
            updateBarberUseCase,
            addBarberUseCase,
            logoutUseCase
        )

        viewModel.openAddDialog()
        // Invalid input (empty name)
        viewModel.onConfirmDialog("", "test@example.com", "1234567890", "password123")

        val message = viewModel.messageChannel.first()
        assertTrue(message.isError)
        assertTrue(message.message.contains("Falta el nombre"))
        
        // Dialog should be closed (based on current implementation logic, although ideally it should stay open on error)
        // Re-reading logic: actually it says _showDialog.value = false inside the if block for errors
        assertFalse(viewModel.showDialog.value)
    }

    @Test
    fun `add new barber success`() = runTest {
        coEvery { getBarbersUseCase() } returns Result.success(emptyList())
        coEvery { addBarberUseCase(any()) } returns Result.success(mockk())
        
        viewModel = ManageBarbersViewModel(
            getBarbersUseCase,
            deleteBarberUseCase,
            updateBarberUseCase,
            addBarberUseCase,
            logoutUseCase
        )

        viewModel.openAddDialog()
        viewModel.onConfirmDialog("New Barber", "new@example.com", "1234567890", "password123")

        coVerify { addBarberUseCase(any()) }
        coVerify(exactly = 2) { getBarbersUseCase() } // Once on init, once after save
        
        val message = viewModel.messageChannel.first()
        assertEquals("Barbero creado", message.message)
        assertFalse(message.isError)
    }

    @Test
    fun `update existing barber success`() = runTest {
        val existingBarber = Barber(1, "Old Name", "old@example.com", "oldpass", "1234567890", true)
        coEvery { getBarbersUseCase() } returns Result.success(listOf(existingBarber))
        coEvery { updateBarberUseCase(any()) } returns Result.success(mockk())

        viewModel = ManageBarbersViewModel(
            getBarbersUseCase,
            deleteBarberUseCase,
            updateBarberUseCase,
            addBarberUseCase,
            logoutUseCase
        )

        viewModel.openEditDialog(existingBarber)
        // Password empty means keep existing
        viewModel.onConfirmDialog("New Name", "new@example.com", "0987654321", "")

        coVerify(exactly = 1) {
            updateBarberUseCase(match { 
                it.id == existingBarber.id && 
                it.name == "New Name" &&
                it.phone == "0987654321" &&
                it.password == "oldpass" 
            }) 
        }
        
        val message = viewModel.messageChannel.first()
        assertEquals("Barbero actualizado", message.message)
    }

    @Test
    fun `delete barber success`() = runTest {
        coEvery { getBarbersUseCase() } returns Result.success(emptyList())
        coEvery { deleteBarberUseCase(1) } returns Result.success(Unit)

        viewModel = ManageBarbersViewModel(
            getBarbersUseCase,
            deleteBarberUseCase,
            updateBarberUseCase,
            addBarberUseCase,
            logoutUseCase
        )

        viewModel.deleteBarber(1)

        coVerify { deleteBarberUseCase(1) }
        coVerify(exactly = 2) { getBarbersUseCase() }

        val message = viewModel.messageChannel.first()
        assertEquals("Barbero eliminado", message.message)
    }
}
