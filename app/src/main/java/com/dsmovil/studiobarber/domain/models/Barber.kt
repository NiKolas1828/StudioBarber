package com.dsmovil.studiobarber.domain.models

data class Barber (
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val isActive: Boolean = true
)