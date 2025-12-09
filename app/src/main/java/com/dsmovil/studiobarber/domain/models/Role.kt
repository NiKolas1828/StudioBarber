package com.dsmovil.studiobarber.domain.models

enum class Role {
    CLIENTE,
    BARBERO,
    ADMINISTRADOR;

    companion object {
        fun fromString(roleName: String?): Role {
            return when (roleName?.uppercase()) {
                "ROLE_CLIENTE" -> CLIENTE
                "ROLE_EMPLEADO" -> BARBERO
                "ROLE_ADMINISTRADOR" -> ADMINISTRADOR
                else -> throw IllegalArgumentException("Unknown role: $roleName")
            }
        }
    }
}