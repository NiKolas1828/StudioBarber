package com.dsmovil.studiobarber.data.remote.models.reservations

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

data class ReservationRequest(
    @SerializedName("serviceId") val serviceId: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("barberId") val barberId: Long,
    @SerializedName("date") val date: String,
    @SerializedName("timeStart") val timeStart: String
)
