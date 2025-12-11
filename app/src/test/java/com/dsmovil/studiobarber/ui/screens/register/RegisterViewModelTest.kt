package com.dsmovil.studiobarber.ui.screens.register

import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.domain.models.User
import com.dsmovil.studiobarber.domain.usecases.RegisterUseCase
import com.dsmovil.studiobarber.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val registerUseCase = mockk<RegisterUseCase>()
    private lateinit var viewModel: RegisterViewModel

    @Test
    fun `initial state is correct`() {
        viewModel = RegisterViewModel(registerUseCase)
        val state = viewModel.uiState
        
        assertEquals("", state.firstname)
        assertEquals("", state.lastname)
        assertEquals("", state.phone)
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals("", state.confirmPassword)
        assertFalse(state.loading)
        assertNull(state.error)
        assertFalse(state.success)
    }

    @Test
    fun `input changes update state correctly`() {
        viewModel = RegisterViewModel(registerUseCase)

        viewModel.onFirstNameChange("John")
        assertEquals("John", viewModel.uiState.firstname)

        viewModel.onLastNameChange("Doe")
        assertEquals("Doe", viewModel.uiState.lastname)

        viewModel.onPhoneChange("123456789")
        assertEquals("123456789", viewModel.uiState.phone)

        viewModel.onEmailChange("john@example.com")
        assertEquals("john@example.com", viewModel.uiState.email)

        viewModel.onPasswordChange("password123")
        assertEquals("password123", viewModel.uiState.password)

        viewModel.onConfirmPasswordChange("password123")
        assertEquals("password123", viewModel.uiState.confirmPassword)
    }

    @Test
    fun `register success updates state to success`() = runTest {
        val user = User(1, "John Doe", "john@example.com", Role.CLIENTE)
        coEvery { 
            registerUseCase("John", "Doe", "123456789", "john@example.com", "password123", "password123") 
        } returns Result.success(user)
        
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.onFirstNameChange("John")
        viewModel.onLastNameChange("Doe")
        viewModel.onPhoneChange("123456789")
        viewModel.onEmailChange("john@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")

        viewModel.register()

        assertTrue(viewModel.uiState.success)
        assertFalse(viewModel.uiState.loading)
        assertNull(viewModel.uiState.error)
    }

    @Test
    fun `register failure updates state with error`() = runTest {
        val errorMessage = "Registration failed"
        coEvery { 
            registerUseCase("John", "Doe", "123456789", "john@example.com", "password123", "password123") 
        } returns Result.failure(Exception(errorMessage))
        
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.onFirstNameChange("John")
        viewModel.onLastNameChange("Doe")
        viewModel.onPhoneChange("123456789")
        viewModel.onEmailChange("john@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")

        viewModel.register()

        assertFalse(viewModel.uiState.success)
        assertFalse(viewModel.uiState.loading)
        assertEquals(errorMessage, viewModel.uiState.error)
    }
}
