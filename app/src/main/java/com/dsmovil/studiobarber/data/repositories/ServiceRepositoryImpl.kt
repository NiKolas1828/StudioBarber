package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.remote.models.service.ServiceRequest
import com.dsmovil.studiobarber.data.remote.models.service.toDomain
import com.dsmovil.studiobarber.data.remote.service.ServiceApiService
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.domain.repositories.ServiceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepositoryImpl @Inject constructor(
    private val apiService: ServiceApiService
) : ServiceRepository {
    private fun createCommonErrorMessage(code: Int): String {
        return when (code) {
            401 -> "El usuario no está autenticado"
            403 -> "El usuario no tiene permisos para acceder a este recurso"
            else -> "Ocurrió un error inesperado ($code)"
        }
    }

    override suspend fun getServices(): Result<List<Service>> {
        return try {
            val response = apiService.getServices()

            if (response.isSuccessful && response.body() != null) {
                val servicesResponse = response.body()!!

                val services = servicesResponse.map { it.toDomain() }

                Result.success(services)
            } else {
                val errorMsg = createCommonErrorMessage(response.code())

                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addService(service: Service): Result<Unit> {
        return try {
            val request = ServiceRequest(
                name = service.name,
                description = service.description,
                price = service.price,
            )

            val response = apiService.createService(request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    409 -> "Ya existe un servicio con este nombre"
                    else -> createCommonErrorMessage(response.code())
                }
                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateService(service: Service): Result<Unit> {
        return try {
            val request = ServiceRequest(
                name = service.name,
                description = service.description,
                price = service.price
            )

            val response = apiService.updateService(service.id, request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    404 -> "Servicio no encontrado"
                    409 -> "Ya existe otro servicio con este nombre"
                    else -> createCommonErrorMessage(response.code())
                }
                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteService(id: Long): Result<Unit> {
        return try {
            val response = apiService.deleteService(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    404 -> "Servicio no encontrado"
                    else -> createCommonErrorMessage(response.code())
                }
                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}