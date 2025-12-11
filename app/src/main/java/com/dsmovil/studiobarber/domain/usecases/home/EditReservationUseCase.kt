package com.dsmovil.studiobarber.domain.usecases.home

import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationRequest
import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationResponse
import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import javax.inject.Inject

class EditReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(id:Long, request: ReservationRequest): Result<ReservationResponse> {
        return reservationRepository.editReservation(id, request)
    }
}