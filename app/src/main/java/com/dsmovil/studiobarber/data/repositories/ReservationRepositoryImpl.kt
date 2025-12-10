package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.remote.models.reservations.toDomain
import com.dsmovil.studiobarber.data.remote.reservation.ReservationApiService
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepositoryImpl @Inject constructor(
    private val apiService: ReservationApiService
): ReservationRepository {


    override suspend fun getReservations(id: Long): Result<List<Reservation>> {
        return try {
            val response = apiService.getById(id)

            if (response.isSuccessful && response.body() != null) {
                val reservationsResponse = response.body()!!

                val reservations = reservationsResponse.map {it.toDomain()}

                Result.success(reservations)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "El usuario no está autenticado"
                    403 -> "El usuario no tiene permisos para acceder a este recurso"
                    else -> "Ocurrió un error inesperado (${response.code()})"
                }

                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteReservation(id: Long): Result<Unit> {
        return try {
            val response = apiService.cancel(id)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "El usuario no está autenticado"
                    403 -> "El usuario no tiene permisos para acceder a este recurso"
                    else -> "Ocurrió un error inesperado (${response.code()})"
                }

                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)

        }
    }
}