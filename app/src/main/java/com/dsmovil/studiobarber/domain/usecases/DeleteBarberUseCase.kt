package com.dsmovil.studiobarber.domain.usecases

import com.dsmovil.studiobarber.domain.repositories.BarberRepository
import javax.inject.Inject

class DeleteBarberUseCase @Inject constructor(
    private val barberRepository: BarberRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return barberRepository.deleteBarber(id)
    }
}