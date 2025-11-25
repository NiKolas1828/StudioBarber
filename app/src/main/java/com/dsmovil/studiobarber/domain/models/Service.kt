package com.dsmovil.studiobarber.domain.models

data class Service (
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val isActive: Boolean = true
)