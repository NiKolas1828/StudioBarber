package com.dsmovil.studiobarber.domain.usecases.admin.services

import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.repositories.ServiceRepository
import javax.inject.Inject

class AddServiceUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(service: Service): Result<Unit> {
        return serviceRepository.addService(service)
    }
}