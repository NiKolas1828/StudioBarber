package com.dsmovil.studiobarber.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsmovil.studiobarber.data.repositories.AuthRepositoryImpl
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val repository = AuthRepositoryImpl()
            val loginUseCase = LoginUseCase(repository)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}