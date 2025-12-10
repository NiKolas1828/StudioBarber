package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.remote.barber.BarberApiService
import com.dsmovil.studiobarber.data.remote.models.barber.CreateBarberRequest
import com.dsmovil.studiobarber.data.remote.models.barber.UpdateBarberRequest
import com.dsmovil.studiobarber.data.remote.models.barber.toDomain
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.repositories.BarberRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarberRepositoryImpl @Inject constructor(
    private val apiService: BarberApiService
) : BarberRepository {
    private fun createCommonErrorMessage(code: Int): String {
        return when (code) {
            401 -> "El usuario no está autenticado"
            403 -> "El usuario no tiene permisos para acceder a este recurso"
            else -> "Ocurrió un error inesperado ($code)"
        }
    }

    override suspend fun getBarbers(): Result<List<Barber>> {
        return try {
            val response = apiService.getBarbers()

            if (response.isSuccessful && response.body() != null) {
                val barbersResponse = response.body()!!

                val barbers = barbersResponse.map { it.toDomain() }

                Result.success(barbers)
            } else {
                val errorMsg = createCommonErrorMessage(response.code())

                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBarber(id: Long): Result<Unit> {
        return try {
            val response = apiService.cancelBarber(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = createCommonErrorMessage(response.code())

                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addBarber(barber: Barber): Result<Unit> {
        return try {
            val request = CreateBarberRequest(
                name = barber.name,
                email = barber.email,
                password = barber.password,
                phoneNumber = barber.phone,
            )

            val response = apiService.createBarber(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    422 -> "El correo del barbero ya está registrado"
                    else -> createCommonErrorMessage(response.code())
                }

                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBarber(barber: Barber): Result<Unit> {
        return try {
            val request = UpdateBarberRequest(
                id = barber.id,
                name = barber.name,
                email = barber.email,
                phoneNumber = barber.phone,
            )

            val response = apiService.updateBarber(request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    404 -> "Barbero no encontrado"
                    else -> createCommonErrorMessage(response.code())
                }

                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
