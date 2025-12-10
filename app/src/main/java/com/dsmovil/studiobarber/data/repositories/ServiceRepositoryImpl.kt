package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.repositories.ServiceRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepositoryImpl @Inject constructor() : ServiceRepository {
    private val mockServices = mutableListOf(
        Service(1, "Corte de Cabello", "Corte clásico o moderno con tijera/máquina", 15.00),
        Service(2, "Afeitado de Barba", "Afeitado con toalla caliente y navaja", 10.00),
        Service(5, "Perfilado de Cejas", "Limpieza y perfilado con navaja", 5.00)
    )

    override suspend fun getServices(): Result<List<Service>> {
        delay(500)

        return Result.success(mockServices.toList())
    }

    override suspend fun addService(service: Service): Result<Unit> {
        delay(300)
        val newId = (mockServices.maxOfOrNull { it.id } ?: 0) + 1
        mockServices.add(service.copy(id = newId))

        return Result.success(Unit)
    }

    override suspend fun updateService(service: Service): Result<Unit> {
        delay(300)
        val index = mockServices.indexOfFirst { it.id == service.id }

        if (index != -1) {
            mockServices[index] = service
            return Result.success(Unit)
        }

        return Result.failure(Exception("Servicio no encontrado"))
    }

    override suspend fun deleteService(id: Long): Result<Unit> {
        delay(300)
        mockServices.removeIf { it.id == id }

        return Result.success(Unit)
    }
}