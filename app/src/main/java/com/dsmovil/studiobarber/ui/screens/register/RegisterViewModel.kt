package com.dsmovil.studiobarber.ui.screens.register
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import com.dsmovil.studiobarber.di.AppModule.loginUseCase
import com.dsmovil.studiobarber.domain.usecases.RegisterUseCase
import com.dsmovil.studiobarber.ui.screens.register.RegisterUiState

import kotlinx.coroutines.launch


class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onFirstNameChange(value: String) {
        uiState = uiState.copy(firstname = value)
    }

    fun onLastNameChange(value: String) {
        uiState = uiState.copy(lastname = value)
    }

    fun onPhoneChange(value: String) {
        uiState = uiState.copy(phone = value)
    }

    fun onConfirmPasswordChange(value: String) {
        uiState = uiState.copy(confirmPassword = value)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value)
    }
    fun register() {
        // Lógica de registro aquí
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)

            val result = registerUseCase(uiState.firstname,uiState.lastname, uiState.phone ,uiState.email, uiState.password, uiState.confirmPassword)

            uiState = if (result.isSuccess) {
                uiState.copy(loading = false, success = true)
            } else {
                uiState.copy(
                    loading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}
