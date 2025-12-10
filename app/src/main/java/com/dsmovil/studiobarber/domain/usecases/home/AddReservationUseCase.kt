package com.dsmovil.studiobarber.domain.usecases.home

import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationRequest
import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationResponse
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import javax.inject.Inject

class AddReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(reservation: ReservationRequest): Result<ReservationResponse>{
        return reservationRepository.addReservation(reservation)
    }
}