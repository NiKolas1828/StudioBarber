package com.dsmovil.studiobarber.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import com.dsmovil.studiobarber.di.AppModule.loginUseCase

import kotlinx.coroutines.launch


class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun login() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)

            val result = loginUseCase(uiState.email, uiState.password)

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
