package com.dsmovil.studiobarber.domain.repositories

import com.dsmovil.studiobarber.domain.models.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>

    suspend fun register(firstname: String, lastname: String, phone: String, email: String, password: String, confirmPassword: String): Result<User>
}