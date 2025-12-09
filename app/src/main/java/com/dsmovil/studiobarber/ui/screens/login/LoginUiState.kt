package com.dsmovil.studiobarber.ui.screens.login

import com.dsmovil.studiobarber.domain.models.User

sealed class LoginUiState {
    data object Loading: LoginUiState()
    data class Success(
        val user: User
    ) : LoginUiState()
    data class Error(
        val message: String
    ) : LoginUiState()
}