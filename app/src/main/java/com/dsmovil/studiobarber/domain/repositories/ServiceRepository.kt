package com.dsmovil.studiobarber.domain.repositories

import com.dsmovil.studiobarber.domain.models.Service

interface ServiceRepository {
    suspend fun getServices(): Result<List<Service>>
    suspend fun addService(service: Service): Result<Unit>
    suspend fun updateService(service: Service): Result<Unit>
    suspend fun deleteService(id: Long): Result<Unit>
}