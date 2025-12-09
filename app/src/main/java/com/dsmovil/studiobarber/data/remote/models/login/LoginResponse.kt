package com.dsmovil.studiobarber.data.remote.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("userId") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: List<String>
)
