package com.dsmovil.studiobarber.ui.screens.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState?>(null)
    val uiState: StateFlow<LoginUiState?> = _uiState.asStateFlow()

    private fun validateInput(email: String, password: String): List<String> {
        val errors = mutableListOf<String>()

        if (email.isBlank()) {
            errors.add("• Falta el correo")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add("• Correo inválido")
        }

        if (password.length < 8) {
            errors.add("• La contraseña debe ser al menos de 8 caracteres")
        }

        return errors
    }

    fun login(email: String, password: String) {
        val validationErrors = validateInput(email, password)

        if (validationErrors.isNotEmpty()) {
            val finalMessage = validationErrors.joinToString("\n")
            _uiState.value = LoginUiState.Error(message = finalMessage)
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = loginUseCase(email, password)

            result.fold(
                onSuccess = { user ->
                    _uiState.value = LoginUiState.Success(user = user)
                },
                onFailure = { exception ->
                    _uiState.value = LoginUiState.Error(message = exception.message ?: "Ocurrio un error inesperado")
                }
            )
        }
    }
}
