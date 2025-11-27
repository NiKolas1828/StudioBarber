package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.local.MemoryDatabase
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepositoryImpl @Inject constructor(
    private val db: MemoryDatabase
) : ReservationRepository {

    override suspend fun getReservations(): Result<List<Reservation>> {
        delay(500)
        return Result.success(db.reservations.toList())
    }

    override suspend fun deleteReservation(id: Long): Result<Unit> {
        delay(300)
        val removed = db.reservations.removeIf { it.id == id }
        return if (removed) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Reserva no encontrada"))
        }
    }

    override suspend fun createReservation(reservation: Reservation): Result<Unit> {
        delay(400)

        val newId = (db.reservations.maxOfOrNull { it.id } ?: 0) + 1
        val newReservation = reservation.copy(id = newId)

        db.reservations.add(newReservation)
        return Result.success(Unit)
    }

}