package com.dsmovil.studiobarber.domain.usecases.admin.services

import com.dsmovil.studiobarber.domain.repositories.ServiceRepository
import javax.inject.Inject

class DeleteServiceUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return serviceRepository.deleteService(id)
    }
}