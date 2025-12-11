package com.dsmovil.studiobarber.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.models.Register
import com.dsmovil.studiobarber.domain.usecases.RegisterUseCase
import com.dsmovil.studiobarber.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState?>(null)
    val uiState: StateFlow<RegisterUiState?> = _uiState.asStateFlow()

    private fun validateInput(
        name: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String
    ): List<String> {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("• El nombre no puede estar vacío")
        }

        if (phone.isBlank()) {
            errors.add("• El teléfono no puede estar vacío")
        } else if (phone.length != 10) {
            errors.add("• Teléfono inválido")
        }

        if (email.isBlank()) {
            errors.add("• El correo no puede estar vacío")
        } else if (!isValidEmail(email)) {
            errors.add("• El formato del correo es inválido")
        }

        if (password.length < 8) {
            errors.add("• La contraseña debe tener al menos 8 caracteres")
        }

        if (password != confirmPassword) {
            errors.add("• Las contraseñas no coinciden")
        }

        return errors
    }

    fun register(
        name: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val validationErrors = validateInput(name, phone, email, password, confirmPassword)

        if (validationErrors.isNotEmpty()) {
            val finalMessage = validationErrors.joinToString("\n")
            _uiState.value = RegisterUiState.Error(message = finalMessage)
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            val registerData = Register(
                name = name,
                phone = phone,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )

            val result = registerUseCase(registerData)

            if (result.isSuccess) {
                _uiState.value = RegisterUiState.Success
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                _uiState.value = RegisterUiState.Error(message = errorMessage)
            }
        }
    }
}
