package com.dsmovil.studiobarber.ui.screens.register

import com.dsmovil.studiobarber.domain.models.Register
import com.dsmovil.studiobarber.domain.usecases.RegisterUseCase
import com.dsmovil.studiobarber.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun `initial state is null`() {
        viewModel = RegisterViewModel(registerUseCase)
        val initialState = viewModel.uiState.value
        assertNull("El estado inicial debería ser null", initialState)
    }

    @Test
    fun `register success transitions state through Loading to Success`() = runTest {
        // Datos de prueba
        val registerData = Register(
            name = "John Doe",
            phone = "1234567890",
            email = "john@example.com",
            password = "password123",
            confirmPassword = "password123"
        )

        // Simula el caso de uso exitoso
        coEvery { registerUseCase(registerData) } returns Result.success(Unit)

        viewModel = RegisterViewModel(registerUseCase)

        // Llama a la función de registro
        viewModel.register(
            name = registerData.name,
            phone = registerData.phone,
            email = registerData.email,
            password = registerData.password,
            confirmPassword = registerData.confirmPassword
        )

        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertTrue("El estado final debería ser Success", finalState is RegisterUiState.Success)

        coVerify(exactly = 1) { registerUseCase(registerData) }
    }

    @Test
    fun `register failure transitions state through Loading to Error`() = runTest {
        val errorMessage = "El correo ya existe"
        val registerData = Register(
            name = "John Doe",
            phone = "1234567890",
            email = "john@example.com",
            password = "password123",
            confirmPassword = "password123"
        )

        // Simula el caso de uso fallido
        coEvery { registerUseCase(registerData) } returns Result.failure(Exception(errorMessage))

        viewModel = RegisterViewModel(registerUseCase)

        viewModel.register(
            name = registerData.name,
            phone = registerData.phone,
            email = registerData.email,
            password = registerData.password,
            confirmPassword = registerData.confirmPassword
        )

        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertTrue("El estado final debería ser Error", finalState is RegisterUiState.Error)
        assertEquals(errorMessage, (finalState as RegisterUiState.Error).message)

        coVerify(exactly = 1) { registerUseCase(registerData) }
    }

    // Inicio De Tests De Validación

    @Test
    fun `register with blank name returns validation error`() = runTest {
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.register(name = "", phone = "1234567890", email = "test@test.com", password = "password123", confirmPassword = "password123")
        val state = viewModel.uiState.value
        assertTrue(state is RegisterUiState.Error && state.message.contains("El nombre no puede estar vacío"))
        coVerify(exactly = 0) { registerUseCase(any()) }
    }

    @Test
    fun `register with blank phone returns validation error`() = runTest {
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.register(name = "Test User", phone = "", email = "test@test.com", password = "password123", confirmPassword = "password123")
        val state = viewModel.uiState.value
        assertTrue(state is RegisterUiState.Error && state.message.contains("El teléfono no puede estar vacío"))
        coVerify(exactly = 0) { registerUseCase(any()) }
    }

    @Test
    fun `register with invalid phone length returns validation error`() = runTest {
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.register(name = "Test User", phone = "123", email = "test@test.com", password = "password123", confirmPassword = "password123")
        val state = viewModel.uiState.value
        assertTrue(state is RegisterUiState.Error && state.message.contains("Teléfono inválido"))
        coVerify(exactly = 0) { registerUseCase(any()) }
    }

    @Test
    fun `register with blank email returns validation error`() = runTest {
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.register(name = "Test User", phone = "1234567890", email = "", password = "password123", confirmPassword = "password123")
        val state = viewModel.uiState.value
        assertTrue(state is RegisterUiState.Error && state.message.contains("El correo no puede estar vacío"))
        coVerify(exactly = 0) { registerUseCase(any()) }
    }

    @Test
    fun `register with invalid email format returns validation error`() = runTest {
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.register(name = "Test User", phone = "1234567890", email = "test.com", password = "password123", confirmPassword = "password123")
        val state = viewModel.uiState.value
        assertTrue(state is RegisterUiState.Error && state.message.contains("El formato del correo es inválido"))
        coVerify(exactly = 0) { registerUseCase(any()) }
    }

    @Test
    fun `register with short password returns validation error`() = runTest {
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.register(name = "Test User", phone = "1234567890", email = "test@test.com", password = "123", confirmPassword = "123")
        val state = viewModel.uiState.value
        assertTrue(state is RegisterUiState.Error && state.message.contains("La contraseña debe tener al menos 8 caracteres"))
        coVerify(exactly = 0) { registerUseCase(any()) }
    }

    @Test
    fun `register with mismatched passwords returns validation error`() = runTest {
        viewModel = RegisterViewModel(registerUseCase)
        viewModel.register(name = "Test User", phone = "1234567890", email = "test@test.com", password = "password123", confirmPassword = "password456")
        val state = viewModel.uiState.value
        assertTrue(state is RegisterUiState.Error && state.message.contains("Las contraseñas no coinciden"))
        coVerify(exactly = 0) { registerUseCase(any()) }
    }
}
