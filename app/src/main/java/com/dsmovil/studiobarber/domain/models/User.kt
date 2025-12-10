package com.dsmovil.studiobarber.domain.models

data class User (

    val id: Long,
    val name: String,
    val email: String,
    val role: Role
)