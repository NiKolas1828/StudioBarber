package com.dsmovil.studiobarber.data.remote.models.barber

import com.dsmovil.studiobarber.domain.models.Barber
import com.google.gson.annotations.SerializedName

data class BarberResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String
)

fun BarberResponse.toDomain(): Barber {
    return Barber(
        id = this.id,
        name = this.name,
        email = this.email,
        password = "",
        phone = this.phoneNumber
    )
}
