package com.dsmovil.studiobarber.data.remote.models.barber

import com.google.gson.annotations.SerializedName

data class UpdateBarberRequest(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String
)
