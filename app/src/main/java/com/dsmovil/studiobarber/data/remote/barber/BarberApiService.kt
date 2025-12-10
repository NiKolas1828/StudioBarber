package com.dsmovil.studiobarber.data.remote.barber

import com.dsmovil.studiobarber.data.remote.models.barber.BarberResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface BarberApiService {
    @GET("api/users/role/EMPLEADO")
    suspend fun getBarbers(): Response<List<BarberResponse>>

    @PUT("api/users/desactivate/{id}")
    suspend fun cancelBarber(@Path("id") id: Long): Response<Unit>
}