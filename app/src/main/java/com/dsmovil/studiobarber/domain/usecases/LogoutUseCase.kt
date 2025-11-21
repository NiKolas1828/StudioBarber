package com.dsmovil.studiobarber.domain.usecases

import com.dsmovil.studiobarber.data.repositories.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        // TODO: llamar al repositorio para hacer el logout
    }
}