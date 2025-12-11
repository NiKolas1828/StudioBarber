package com.dsmovil.studiobarber.ui.screens.login

import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.domain.models.User
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import com.dsmovil.studiobarber.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase = mockk<LoginUseCase>()
    private lateinit var viewModel: LoginViewModel

    @Test
    fun `login with empty email returns error`() = runTest {
        viewModel = LoginViewModel(loginUseCase)
        
        viewModel.login("", "password123")
        
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("• Falta el correo", (state as LoginUiState.Error).message)
    }

    @Test
    fun `login with invalid email returns error`() = runTest {
        viewModel = LoginViewModel(loginUseCase)
        
        viewModel.login("invalid-email", "password123")
        
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("• Correo inválido", (state as LoginUiState.Error).message)
    }

    @Test
    fun `login with short password returns error`() = runTest {
        viewModel = LoginViewModel(loginUseCase)
        
        viewModel.login("test@example.com", "short")
        
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("• La contraseña debe ser al menos de 8 caracteres", (state as LoginUiState.Error).message)
    }

    @Test
    fun `login success updates state to Success`() = runTest {
        val user = User(1, "Test User", "test@example.com", Role.CLIENTE)
        coEvery { loginUseCase("test@example.com", "password123") } returns Result.success(user)
        viewModel = LoginViewModel(loginUseCase)

        viewModel.login("test@example.com", "password123")

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals(user, (state as LoginUiState.Success).user)
    }

    @Test
    fun `login failure updates state to Error`() = runTest {
        val errorMessage = "Invalid credentials"
        coEvery { loginUseCase("test@example.com", "wrongpassword") } returns Result.failure(Exception(errorMessage))
        viewModel = LoginViewModel(loginUseCase)

        viewModel.login("test@example.com", "wrongpassword")

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals(errorMessage, (state as LoginUiState.Error).message)
    }
}
