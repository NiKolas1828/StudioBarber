package com.dsmovil.studiobarber.data.remote.models.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone_number") val phone: String,
)
