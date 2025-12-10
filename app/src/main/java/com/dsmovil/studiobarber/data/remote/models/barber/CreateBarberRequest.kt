package com.dsmovil.studiobarber.data.remote.models.barber

import com.google.gson.annotations.SerializedName

data class CreateBarberRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("role") val role: String = "empleado"
)
