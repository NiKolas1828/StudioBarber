package com.dsmovil.studiobarber.data.remote.barber

import com.dsmovil.studiobarber.data.remote.models.barber.BarberResponse
import com.dsmovil.studiobarber.data.remote.models.barber.CreateBarberRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BarberApiService {
    @GET("api/users/role/EMPLEADO")
    suspend fun getBarbers(): Response<List<BarberResponse>>

    @POST("api/users/create")
    suspend fun createBarber(@Body request: CreateBarberRequest): Response<BarberResponse>
}