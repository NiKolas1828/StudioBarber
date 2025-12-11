package com.dsmovil.studiobarber.domain.repositories

import com.dsmovil.studiobarber.domain.models.Register
import com.dsmovil.studiobarber.domain.models.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>

    suspend fun register(register: Register): Result<Unit>
}