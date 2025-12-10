package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.remote.barber.BarberApiService
import com.dsmovil.studiobarber.data.remote.models.barber.CreateBarberRequest
import com.dsmovil.studiobarber.data.remote.models.barber.toDomain
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.repositories.BarberRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarberRepositoryImpl @Inject constructor(
    private val apiService: BarberApiService
) : BarberRepository {
    private val mockBarbers = mutableListOf(
        Barber(1, "Juan", "juan@gmail.com", "12345", "3125178190"),
        Barber(2, "Nicolas", "nicolas@gmail.com", "12345", "3208147189"),
        Barber(3, "Alexis", "alexis@gmail.com", "12345", "3004178190"),
        Barber(4, "Ander", "ander@gmail.com", "12345", "3114517518")
    )

    private fun createCommonErrorMessage(code: Int): String {
        return when (code) {
            401 -> "El usuario no está autenticado"
            403 -> "El usuario no tiene permisos para acceder a este recurso"
            else -> "Ocurrió un error inesperado (${code})"
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

    override suspend fun getBarberById(id: Long): Result<Barber> {
        delay(300)
        val barber = mockBarbers.find { it.id == id }

        return if (barber != null) {
            Result.success(barber)
        } else {
            Result.failure(Exception("Barbero no encontrado"))
        }
    }

    override suspend fun updateBarber(barber: Barber): Result<Unit> {
        delay(300)
        val index = mockBarbers.indexOfFirst { it.id == barber.id }

        return if (index != -1) {
            mockBarbers[index] = barber
            Result.success(Unit)
        } else {
            Result.failure(Exception("No se pudo actualizar"))
        }
    }
}