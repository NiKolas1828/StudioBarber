package com.dsmovil.studiobarber.domain.repositories

import com.dsmovil.studiobarber.domain.models.Reservation

interface ReservationRepository {

    suspend fun getReservations(): Result<List<Reservation>>

    suspend fun deleteReservation(id: Long): Result<Unit>
}