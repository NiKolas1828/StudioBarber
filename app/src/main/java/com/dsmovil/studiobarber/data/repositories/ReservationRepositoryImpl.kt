package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepositoryImpl @Inject constructor() : ReservationRepository{

    private val mockReservations = mutableListOf(
        Reservation(
            1, 10, "Corte clasico", 2, "Juan Perez",
            3, "Carlos Cardona", LocalDate.of(2025, 11, 20),
            timeStart = LocalTime.of(14, 30), true, true, true
        ),
        Reservation(
            2, 11, "Afeitado premium", 4, "Luis Martinez",
            5, "Andres Torres", LocalDate.of(2025, 11, 21),
            timeStart = LocalTime.of(9, 0), true, true, false
        ),
        Reservation(
            3, 12, "Tinte de cabello", 6, "Maria Lopez",
            7, "Fernando Diaz", LocalDate.of(2025, 11, 22),
            timeStart = LocalTime.of(11, 15), false, true, true
        ),
        Reservation(
            4, 13, "Arreglo de barba", 8, "Pedro Sanchez",
            9, "Jorge Ramirez", LocalDate.of(2025, 11, 23),
            timeStart = LocalTime.of(16, 45), true, false, true
        )

    )

    override suspend fun getReservations(): Result<List<Reservation>> {
        delay(500) // Simular un retraso de red

        return Result.success(mockReservations.toList())
    }

    override suspend fun deleteReservation(id: Long): Result<Unit> {
        delay(300)
        val removed = mockReservations.removeIf { it.id == id }

        return if (removed) Result.success(Unit) else Result.failure(Exception("No encontrado"))
    }
}