package com.dsmovil.studiobarber.domain.repositories

import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationRequest
import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationResponse
import com.dsmovil.studiobarber.domain.models.Reservation

interface ReservationRepository {

    suspend fun getReservations(id: Long): Result<List<Reservation>>

    suspend fun deleteReservation(id: Long): Result<Unit>

    suspend fun addReservation(reservation: ReservationRequest): Result<ReservationResponse>

    suspend fun getAllReservations(): Result<List<Reservation>>
}