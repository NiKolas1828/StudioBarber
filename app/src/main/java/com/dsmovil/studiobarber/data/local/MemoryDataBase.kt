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
        User(id = "1", name = "Nicolas", email = "nicolas@gmail.com", password = "123456", phoneNumber = "3214587412",Role.CLIENTE),
        User(id = "2", name = "Juan Miguel", email="juanmi@gmail.com" ,password = "admin", phoneNumber = "3152487569",Role.ADMINISTRADOR),
        User(id = "3", name = "Alexis", email="alexis@gmail.com" ,password = "123456", phoneNumber = "3125694879",Role.BARBERO)
    )

    val barbers = mutableListOf(
        Barber(1, "Carlos Martínez", "carlos@studio.com", "123456", "3125550001", true),
        Barber(2, "Juan Pérez", "juan@studio.com", "123456", "3125550002", true),
        Barber(3, "Alexis", "alexis@studio.com", "123456", "3125550002", true)
    )

    val services = mutableListOf(
        Service(1, "Corte de Cabello", "Corte clásico o moderno", 15.0),
        Service(2, "Afeitado de Barba", "Afeitado con toalla caliente", 10.0),
        Service(3, "Depilado de Cejas", "Depilado elegante de cejas", 5.0)
    )

    val reservations = mutableListOf(
        Reservation(
            id = 1,
            serviceId = 1,
            nameService = "Corte de Cabello",
            userId = 2,
            nameUser = "Nicolas",
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
            nameService = "Afeitado de Barba",
            userId = 2,
            nameUser = "Nicolas",
            barberId = 3,
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
            nameUser = "Nicolas",
            barberId = 2,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 11, 29),
            timeStart = LocalTime.of(9, 0),
            status = true,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 4,
            serviceId = 10,
            nameService = "Corte clásico",
            userId = 2,
            nameUser = "Nicolas",
            barberId = 3,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 11, 20),
            timeStart = LocalTime.of(14, 30),
            status = true,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 5,
            serviceId = 11,
            nameService = "Afeitado premium",
            userId = 4,
            nameUser = "Luis Martinez",
            barberId = 5,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 11, 29),
            timeStart = LocalTime.of(9, 0),
            status = true,
            isActive = true,
            amount = false
        ),
        Reservation(
            id = 6,
            serviceId = 12,
            nameService = "Tinte de cabello",
            userId = 6,
            nameUser = "Maria Lopez",
            barberId = 7,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 11, 22),
            timeStart = LocalTime.of(11, 15),
            status = false,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 7,
            serviceId = 13,
            nameService = "Arreglo de barba",
            userId = 8,
            nameUser = "Pedro Sanchez",
            barberId = 9,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 11, 23),
            timeStart = LocalTime.of(16, 45),
            status = true,
            isActive = false,
            amount = true
        ),
        Reservation(
            id = 8,
            serviceId = 14,
            nameService = "Corte clásico",
            userId = 10,
            nameUser = "Santiago Rios",
            barberId = 5,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 12, 1),
            timeStart = LocalTime.of(10, 0),
            status = true,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 9,
            serviceId = 15,
            nameService = "Afeitado premium",
            userId = 11,
            nameUser = "Brayan Morales",
            barberId = 5,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 12, 2),
            timeStart = LocalTime.of(15, 30),
            status = true,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 10,
            serviceId = 16,
            nameService = "Tinte de cabello",
            userId = 12,
            nameUser = "Valentina Torres",
            barberId = 5,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 12, 3),
            timeStart = LocalTime.of(14, 0),
            status = false,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 11,
            serviceId = 17,
            nameService = "Arreglo de barba",
            userId = 13,
            nameUser = "Camilo Ortiz",
            barberId = 5,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 12, 2),
            timeStart = LocalTime.of(17, 0),
            status = true,
            isActive = true,
            amount = true
        ),
        Reservation(
            id = 12,
            serviceId = 18,
            nameService = "Corte clásico",
            userId = 14,
            nameUser = "Andres Garcia",
            barberId = 5,
            nameBarber = "Alexis",
            date = LocalDate.of(2025, 12, 2),
            timeStart = LocalTime.of(9, 30),
            status = true,
            isActive = true,
            amount = true
        )
    )


}