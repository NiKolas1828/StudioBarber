package com.dsmovil.studiobarber.ui.screens.admin.home

import com.dsmovil.studiobarber.data.local.SessionManager
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.ui.screens.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase) {
    private val _uiState = MutableStateFlow("Usuario")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val currentUsername = sessionManager.getCurrentUsername()

        if (currentUsername != null) {
            _uiState.value = currentUsername
        }
    }
}