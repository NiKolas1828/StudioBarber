package com.dsmovil.studiobarber.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    private val _navigationEvent = MutableSharedFlow<Role>()
    val navigationEvent = _navigationEvent.asSharedFlow()

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

            result.onSuccess { user ->
                uiState = uiState.copy(loading = false, success = true)
                _navigationEvent.emit(user.role)
            }.onFailure { exception ->
                uiState = uiState.copy(
                    loading = false,
                    error = exception.message
                )
            }
        }
    }
}
