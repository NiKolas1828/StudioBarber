package com.dsmovil.studiobarber.domain.usecases

import com.dsmovil.studiobarber.domain.repositories.AuthRepository
import com.dsmovil.studiobarber.domain.models.User


class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}
