package com.dsmovil.studiobarber.domain.models

data class Register(
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
