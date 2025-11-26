package com.dsmovil.studiobarber.domain.usecases.admin.services

import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.repositories.ServiceRepository
import javax.inject.Inject

class GetServicesUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(): Result<List<Service>> {
        return serviceRepository.getServices()
    }
}