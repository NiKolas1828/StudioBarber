package com.dsmovil.studiobarber.domain.usecases.home

import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import javax.inject.Inject

class CreateReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(reservation: Reservation): Result<Unit> {
        return reservationRepository.createReservation(reservation)
    }
}