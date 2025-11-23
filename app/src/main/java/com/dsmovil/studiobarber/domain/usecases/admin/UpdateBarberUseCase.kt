package com.dsmovil.studiobarber.domain.usecases.admin

import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.repositories.BarberRepository
import javax.inject.Inject

class UpdateBarberUseCase @Inject constructor(
    private val barberRepository: BarberRepository
) {
    suspend operator fun invoke(barber: Barber): Result<Unit> {
        return barberRepository.updateBarber(barber)
    }
}