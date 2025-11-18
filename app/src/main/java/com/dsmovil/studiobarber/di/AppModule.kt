package com.dsmovil.studiobarber.di

import com.dsmovil.studiobarber.data.repositories.AuthRepository
import com.dsmovil.studiobarber.data.repositories.AuthRepositoryImpl
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import com.dsmovil.studiobarber.domain.usecases.RegisterUseCase
import com.dsmovil.studiobarber.ui.screens.login.LoginViewModel
import com.dsmovil.studiobarber.ui.screens.register.RegisterViewModel

object AppModule {

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }

    val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCase(authRepository)
    }

    fun provideLoginViewModel(): LoginViewModel {
        return LoginViewModel(loginUseCase)
    }

    fun provideRegisterViewModel(): RegisterViewModel {
        return RegisterViewModel(registerUseCase)
    }
}
