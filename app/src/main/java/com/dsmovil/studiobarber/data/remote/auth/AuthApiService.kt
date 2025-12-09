package com.dsmovil.studiobarber.data.remote.auth

import com.dsmovil.studiobarber.data.remote.models.login.LoginRequest
import com.dsmovil.studiobarber.data.remote.models.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}