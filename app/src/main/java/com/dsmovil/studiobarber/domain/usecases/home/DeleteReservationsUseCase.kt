package com.dsmovil.studiobarber.domain.usecases.home

import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import javax.inject.Inject

class DeleteReservationsUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return reservationRepository.deleteReservation(id)
    }
}