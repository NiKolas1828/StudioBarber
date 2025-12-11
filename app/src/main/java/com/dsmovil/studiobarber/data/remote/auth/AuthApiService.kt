package com.dsmovil.studiobarber.data.remote.auth

import com.dsmovil.studiobarber.data.remote.models.login.LoginRequest
import com.dsmovil.studiobarber.data.remote.models.login.LoginResponse
import com.dsmovil.studiobarber.data.remote.models.register.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>
}