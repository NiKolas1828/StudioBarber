package com.dsmovil.studiobarber.data.local

import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.models.User
import java.time.LocalDate
import java.time.LocalTime
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

    val reservations = mutableListOf(
        Reservation(
            id = 1,
            serviceId = 1,
            nameService = "Corte de Cabello",
            userId = 2,
            nameUser = "Cliente Demo",
            barberId = 1,
            nameBarber = "Carlos Martínez",
            date = LocalDate.of(2025, 11, 28),
            timeStart = LocalTime.of(10, 0),
            status = true,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 2,
            serviceId = 2,
            nameService = "Barba",
            userId = 2,
            nameUser = "Cliente Demo",
            barberId = 2,
            nameBarber = "Juan Pérez",
            date = LocalDate.of(2025, 11, 28),
            timeStart = LocalTime.of(11, 30),
            status = false,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 3,
            serviceId = 3,
            nameService = "Corte + Barba",
            userId = 1,
            nameUser = "Admin",
            barberId = 3,
            nameBarber = "Pedro García",
            date = LocalDate.of(2025, 11, 29),
            timeStart = LocalTime.of(9, 0),
            status = true,
            isActive = true,
            amount = true
        )
    )
}