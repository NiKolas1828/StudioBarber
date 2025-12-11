package com.dsmovil.studiobarber.data.remote.service

import com.dsmovil.studiobarber.data.remote.models.service.ServiceRequest
import com.dsmovil.studiobarber.data.remote.models.service.ServiceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceApiService {
    @GET("api/services/")
    suspend fun getServices(): Response<List<ServiceResponse>>

    @POST("api/services/create")
    suspend fun createService(@Body request: ServiceRequest): Response<Unit>

    @PUT("api/services/edit/{id}")
    suspend fun updateService(@Path("id") id: Long, @Body request: ServiceRequest): Response<Unit>

    @DELETE("api/services/{id}")
    suspend fun deleteService(@Path("id") id: Long): Response<Unit>
}