package com.dsmovil.studiobarber.data.remote.models.service

import com.dsmovil.studiobarber.domain.models.Service
import com.google.gson.annotations.SerializedName

data class ServiceResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double
)

fun ServiceResponse.toDomain(): Service {
    return Service(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price
    )
}