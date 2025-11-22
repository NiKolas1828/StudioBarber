package com.dsmovil.studiobarber.domain.usecases

import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.repositories.BarberRepository
import javax.inject.Inject

class GetBarbersUseCase @Inject constructor(
    private val barberRepository: BarberRepository
) {
    suspend operator fun invoke(): Result<List<Barber>> {
        return barberRepository.getBarbers()
    }
}