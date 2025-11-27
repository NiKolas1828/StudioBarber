package com.dsmovil.studiobarber.domain.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val role: Role
)