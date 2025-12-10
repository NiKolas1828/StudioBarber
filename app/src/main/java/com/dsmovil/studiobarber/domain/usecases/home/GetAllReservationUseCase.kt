package com.dsmovil.studiobarber.domain.usecases.home

import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import javax.inject.Inject

class GetAllReservationsUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(): Result<List<Reservation>> {
        return reservationRepository.getAllReservations()
    }
}
