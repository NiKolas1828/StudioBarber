package com.dsmovil.studiobarber.ui.screens.admin.services

import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.AddServiceUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.DeleteServiceUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.GetServicesUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.UpdateServiceUseCase
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
class ManageServicesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getServicesUseCase = mockk<GetServicesUseCase>()
    private val addServiceUseCase = mockk<AddServiceUseCase>()
    private val updateServiceUseCase = mockk<UpdateServiceUseCase>()
    private val deleteServiceUseCase = mockk<DeleteServiceUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)

    private lateinit var viewModel: ManageServicesViewModel

    @Test
    fun `loadServices success updates uiState to Success`() = runTest {
        val services = listOf(
            Service(1, "Haircut", "Standard haircut", 20.0),
            Service(2, "Beard Trim", "Beard trimming", 10.0)
        )
        coEvery { getServicesUseCase() } returns Result.success(services)

        viewModel = ManageServicesViewModel(
            getServicesUseCase,
            addServiceUseCase,
            updateServiceUseCase,
            deleteServiceUseCase,
            logoutUseCase
        )

        val state = viewModel.uiState.value
        assertTrue(state is ManageServicesUiState.Success)
        assertEquals(services, (state as ManageServicesUiState.Success).services)
    }

    @Test
    fun `loadServices failure updates uiState to Error`() = runTest {
        val errorMessage = "Failed to load services"
        coEvery { getServicesUseCase() } returns Result.failure(Exception(errorMessage))

        viewModel = ManageServicesViewModel(
            getServicesUseCase,
            addServiceUseCase,
            updateServiceUseCase,
            deleteServiceUseCase,
            logoutUseCase
        )

        val state = viewModel.uiState.value
        assertTrue(state is ManageServicesUiState.Error)
        assertEquals(errorMessage, (state as ManageServicesUiState.Error).message)
    }

    @Test
    fun `dialog state management works correctly`() = runTest {
        coEvery { getServicesUseCase() } returns Result.success(emptyList())
        viewModel = ManageServicesViewModel(
            getServicesUseCase,
            addServiceUseCase,
            updateServiceUseCase,
            deleteServiceUseCase,
            logoutUseCase
        )

        viewModel.openAddDialog()
        assertTrue(viewModel.showDialog.value)
        assertNull(viewModel.selectedService.value)

        val service = Service(1, "Haircut", "Standard haircut", 20.0)
        viewModel.openEditDialog(service)
        assertTrue(viewModel.showDialog.value)
        assertEquals(service, viewModel.selectedService.value)

        viewModel.closeDialog()
        assertFalse(viewModel.showDialog.value)
        assertNull(viewModel.selectedService.value)
    }

    @Test
    fun `onConfirmDialog validates input and shows error`() = runTest {
        coEvery { getServicesUseCase() } returns Result.success(emptyList())
        viewModel = ManageServicesViewModel(
            getServicesUseCase,
            addServiceUseCase,
            updateServiceUseCase,
            deleteServiceUseCase,
            logoutUseCase
        )

        viewModel.openAddDialog()
        // Invalid input (empty name)
        viewModel.onConfirmDialog("", "Standard haircut", "20.0")

        val message = viewModel.messageChannel.first()
        assertTrue(message.isError)
        assertTrue(message.message.contains("Falta el nombre"))
        
        // Dialog should be closed (based on current implementation logic)
        assertFalse(viewModel.showDialog.value)
    }

    @Test
    fun `add new service success`() = runTest {
        coEvery { getServicesUseCase() } returns Result.success(emptyList())
        coEvery { addServiceUseCase(any()) } returns Result.success(mockk())
        
        viewModel = ManageServicesViewModel(
            getServicesUseCase,
            addServiceUseCase,
            updateServiceUseCase,
            deleteServiceUseCase,
            logoutUseCase
        )

        viewModel.openAddDialog()
        viewModel.onConfirmDialog("New Service", "Description", "30.0")

        coVerify { addServiceUseCase(any()) }
        coVerify(exactly = 2) { getServicesUseCase() } // Once on init, once after save
        
        val message = viewModel.messageChannel.first()
        assertEquals("Servicio creado", message.message)
        assertFalse(message.isError)
    }

    @Test
    fun `update existing service success`() = runTest {
        val existingService = Service(1, "Old Name", "Old Description", 20.0)
        coEvery { getServicesUseCase() } returns Result.success(listOf(existingService))
        coEvery { updateServiceUseCase(any()) } returns Result.success(mockk())

        viewModel = ManageServicesViewModel(
            getServicesUseCase,
            addServiceUseCase,
            updateServiceUseCase,
            deleteServiceUseCase,
            logoutUseCase
        )

        viewModel.openEditDialog(existingService)
        viewModel.onConfirmDialog("New Name", "New Description", "25.0")

        coVerify { 
            updateServiceUseCase(match { 
                it.id == existingService.id && 
                it.name == "New Name" && 
                it.description == "New Description" && 
                it.price == 25.0 
            }) 
        }
        
        val message = viewModel.messageChannel.first()
        assertEquals("Servicio actualizado", message.message)
    }

    @Test
    fun `delete service success`() = runTest {
        coEvery { getServicesUseCase() } returns Result.success(emptyList())
        coEvery { deleteServiceUseCase(1) } returns Result.success(Unit)

        viewModel = ManageServicesViewModel(
            getServicesUseCase,
            addServiceUseCase,
            updateServiceUseCase,
            deleteServiceUseCase,
            logoutUseCase
        )

        viewModel.deleteService(1)

        coVerify { deleteServiceUseCase(1) }
        coVerify(exactly = 2) { getServicesUseCase() }

        val message = viewModel.messageChannel.first()
        assertEquals("Servicio eliminado", message.message)
    }
}
