package com.dsmovil.studiobarber.data.remote

import com.dsmovil.studiobarber.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = sessionManager.getToken() ?: return chain.proceed(originalRequest)

        val authenticatedRequest =
            originalRequest.newBuilder().addHeader("Authorization", "Bearer $token").build()

        return chain.proceed(authenticatedRequest)
    }
}