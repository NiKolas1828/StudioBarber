package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.local.MemoryDatabase // Importamos la base de datos central
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.repositories.ServiceRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepositoryImpl @Inject constructor(
    private val db: MemoryDatabase
) : ServiceRepository {


    override suspend fun getServices(): Result<List<Service>> {
        delay(500)
        return Result.success(db.services.toList())
    }

    override suspend fun addService(service: Service): Result<Unit> {
        delay(300)
        val newId = (db.services.maxOfOrNull { it.id } ?: 0) + 1
        db.services.add(service.copy(id = newId))

        return Result.success(Unit)
    }

    override suspend fun deleteService(id: Long): Result<Unit> {
        delay(300)
        val removed = db.services.removeIf { it.id == id }

        return if (removed) Result.success(Unit) else Result.failure(Exception("Servicio no encontrado"))
    }

    override suspend fun updateService(service: Service): Result<Unit> {
        delay(300)
        val index = db.services.indexOfFirst { it.id == service.id }

        return if (index != -1) {
            db.services[index] = service
            Result.success(Unit)
        } else {
            Result.failure(Exception("No se pudo actualizar"))
        }
    }
}