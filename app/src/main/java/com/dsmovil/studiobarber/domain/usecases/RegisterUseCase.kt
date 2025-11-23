package com.dsmovil.studiobarber.domain.usecases

import com.dsmovil.studiobarber.domain.repositories.AuthRepository
import com.dsmovil.studiobarber.domain.models.User


class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(firstname: String, lastname: String, phone: String,  email: String, password: String, confirmPassword: String): Result<User> {
        return authRepository.register(firstname, lastname, phone, email, password, confirmPassword )
    }
}
