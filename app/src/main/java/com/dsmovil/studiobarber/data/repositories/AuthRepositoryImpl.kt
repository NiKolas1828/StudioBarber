package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.local.SessionManager
import com.dsmovil.studiobarber.data.remote.auth.AuthApiService
import com.dsmovil.studiobarber.data.remote.models.login.LoginRequest
import com.dsmovil.studiobarber.data.remote.models.register.RegisterRequest
import com.dsmovil.studiobarber.domain.models.Register
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
        register: Register
    ): Result<Unit> {
        return try {
            val request = RegisterRequest(
                name = register.name,
                email = register.email,
                password = register.password,
                phone = register.phone
            )

            val response = apiService.register(request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    422 -> "Ya existe un usuario con este correo electrónico."
                    else -> "Ocurrió un error inesperado (${response.code()})"
                }

                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
