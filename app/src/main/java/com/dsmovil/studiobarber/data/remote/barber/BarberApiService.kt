package com.dsmovil.studiobarber.data.remote.barber

import com.dsmovil.studiobarber.data.remote.models.barber.BarberResponse
import com.dsmovil.studiobarber.data.remote.models.barber.CreateBarberRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.POST

interface BarberApiService {
    @GET("api/users/role/EMPLEADO")
    suspend fun getBarbers(): Response<List<BarberResponse>>

    @PUT("api/users/deactivate/{id}")
    suspend fun cancelBarber(@Path("id") id: Long): Response<Unit>
  
    @POST("api/users/create")
    suspend fun createBarber(@Body request: CreateBarberRequest): Response<BarberResponse>
}