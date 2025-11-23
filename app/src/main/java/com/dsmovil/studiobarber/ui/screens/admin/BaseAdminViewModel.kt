package com.dsmovil.studiobarber.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import kotlinx.coroutines.launch

open class BaseAdminViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    fun onLogout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onLogoutSuccess()
        }
    }
}