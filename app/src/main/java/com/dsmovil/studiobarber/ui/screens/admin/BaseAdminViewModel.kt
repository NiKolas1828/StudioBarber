package com.dsmovil.studiobarber.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseAdminViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    fun onLogout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logoutUseCase()
            }

            withContext(Dispatchers.Main) {
                onLogoutSuccess()
            }
        }
    }
}