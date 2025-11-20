package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.domain.models.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return if (username == "test@test.com" && password == "123456") {
            println("Login successful for user: $username")
            Result.success(User("1", "Anderson"))
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun register(
        firstname: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<User> {
        println("registering user with email: $email")
        return if (password == confirmPassword) {
            println("Registration successful for user: $email")
            Result.success(User("2", firstname))
        } else {
            Result.failure(Exception("Passwords do not match"))
        }
    }
}
