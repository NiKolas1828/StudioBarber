package com.dsmovil.studiobarber.data.remote.barber

import com.dsmovil.studiobarber.data.remote.models.barber.BarberResponse
import retrofit2.Response
import retrofit2.http.GET

interface BarberApiService {
    @GET("api/users/role/EMPLEADO")
    suspend fun getBarbers(): Response<List<BarberResponse>>
}