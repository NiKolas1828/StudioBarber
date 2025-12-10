package com.dsmovil.studiobarber.data.remote.models.reservations

import com.dsmovil.studiobarber.domain.models.Reservation
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime
data class ReservationResponse(
    @SerializedName("id") val reservationId: Long,
    @SerializedName("serviceId") val serviceId: Long,
    @SerializedName("nameService") val nameService: String,
    @SerializedName("userId") val userId: Long,
    @SerializedName("nameUser") val nameUser: String,
    @SerializedName("barberId") val barberId: Long,
    @SerializedName("nameBarber") val nameBarber: String,
    @SerializedName("date") val date: String,
    @SerializedName("timeStart") val timeStart: String,
    @SerializedName("status") val status: String,
    @SerializedName("amount") val amount: Double

)

fun ReservationResponse.toDomain(): Reservation {
    return Reservation(
        id = this.reservationId,
        serviceId = this.serviceId,
        nameService = this.nameService,
        userId = this.userId,
        nameUser = this.nameUser,
        barberId = this.barberId,
        nameBarber = this.nameBarber,
        date = LocalDate.parse(this.date),
        timeStart = LocalTime.parse(this.timeStart),
        status = this.status,
        amount = this.amount
    )
}

