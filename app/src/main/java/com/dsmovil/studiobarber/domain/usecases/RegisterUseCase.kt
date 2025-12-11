package com.dsmovil.studiobarber.domain.usecases

import com.dsmovil.studiobarber.domain.models.Register
import com.dsmovil.studiobarber.domain.repositories.AuthRepository


class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(register: Register): Result<Unit> {
        return authRepository.register(register)
    }
}
