package com.dsmovil.studiobarber.ui.screens.login

data class LoginUiState (
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
    )