package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.local.MemoryDatabase
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.domain.models.User
import com.dsmovil.studiobarber.domain.repositories.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val db: MemoryDatabase
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        val user = db.users.find { it.email == username && it.password == password }

        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Credenciales inválidas"))
        }
    }

    override suspend fun register(
        firstname: String, lastname: String, phone: String,
        email: String, password: String, confirmPassword: String
    ): Result<User> {
        if (password != confirmPassword) return Result.failure(Exception("Las contraseñas no coinciden"))

        if (db.users.any { it.email == email }) {
            return Result.failure(Exception("El correo ya está registrado"))
        }

        val newUser = User(
            id = System.currentTimeMillis().toString(),
            name = "$firstname $lastname",
            email = email,
            password = password,
            role = Role.CLIENTE
        )

        db.users.add(newUser)

        return Result.success(newUser)
    }
}