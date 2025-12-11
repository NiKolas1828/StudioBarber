package com.dsmovil.studiobarber.data.remote.reservation

import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationRequest
import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationApiService {
    @POST("api/reservations/create")
    suspend fun create(@Body request: ReservationRequest): Response<ReservationResponse>

    @GET("api/reservations/search-user")
    suspend fun getById(@Query("userId") id: Long): Response<List<ReservationResponse>>

    @PUT("api/reservations/cancel/{id}")
    suspend fun cancel(@Path("id") id: Long): Response<ResponseBody>

    @GET("api/reservations/search-admin")
    suspend fun getAllReservations(): Response<List<ReservationResponse>>
}