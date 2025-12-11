package com.dsmovil.studiobarber.data.remote.models.service

import com.google.gson.annotations.SerializedName

data class ServiceRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("duration") val duration: String = "00:30:00",
    @SerializedName("price") val price: Double
)
