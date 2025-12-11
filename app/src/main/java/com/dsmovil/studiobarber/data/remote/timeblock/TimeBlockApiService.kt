package com.dsmovil.studiobarber.data.remote.timeblock

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TimeBlockApiService {

    @GET("api/time-blocks/availability")
    suspend fun verifyAvailability(
        @Query("idBarber") idBarber: Long,
        @Query("date") date: String,
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): Response<Boolean>
}
