package com.dsmovil.studiobarber.data.local

import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryDatabase @Inject constructor() {

    val users = mutableListOf(
        User(id = "1", name = "Usuario Demo", email = "demo@studio.com", password = "123456", phoneNumber = "3214587412",Role.CLIENTE),
        User(id = "2", name = "Admin", email="admin@studio.com" ,password = "admin", phoneNumber = "3152487569",Role.ADMINISTRADOR)
    )

    val barbers = mutableListOf(
        Barber(1, "Carlos Martínez", "carlos@studio.com", "123456", "3125550001", true),
        Barber(2, "Juan Pérez", "juan@studio.com", "123456", "3125550002", true)
    )

    val services = mutableListOf(
        Service(1, "Corte de Cabello", "Corte clásico o moderno", 15.0),
        Service(2, "Afeitado de Barba", "Afeitado con toalla caliente", 10.0)
    )

    val reservations = mutableListOf<Reservation>()
}