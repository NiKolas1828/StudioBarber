package com.dsmovil.studiobarber.ui.screens.register

sealed class RegisterUiState {
    data object Loading : RegisterUiState()
    data object Success : RegisterUiState()
    data class Error(
        val message: String
    ) : RegisterUiState()
}