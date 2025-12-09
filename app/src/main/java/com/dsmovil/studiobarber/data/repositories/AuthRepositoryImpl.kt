package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.local.SessionManager
import com.dsmovil.studiobarber.data.remote.auth.AuthApiService
import com.dsmovil.studiobarber.data.remote.models.login.LoginRequest
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.domain.models.User
import com.dsmovil.studiobarber.domain.repositories.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val request = LoginRequest(email = username, password = password)

            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!

                val user = User(
                    id = loginResponse.id,
                    name = loginResponse.name,
                    email = loginResponse.email,
                    role = if (loginResponse.role.isNotEmpty())
                        Role.fromString(loginResponse.role[0])
                    else
                        Role.CLIENTE
                )

                sessionManager.saveUser(user = user, token = loginResponse.accessToken)

                Result.success(user)
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "Las credenciales son incorrectas"
                    else -> "Ocurrió un error inesperado (${response.code()})"
                }

                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
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
            Result.success(User(id = 2, name = firstname, email = email, role = Role.CLIENTE))
        } else {
            Result.failure(Exception("Passwords do not match"))
        }
    }
}
