package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationRequest
import com.dsmovil.studiobarber.data.remote.models.reservations.ReservationResponse
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
                response.body()?.close()
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

    override suspend fun addReservation(reservation: ReservationRequest): Result<ReservationResponse> {
        return try {
            val response = apiService.create(reservation)

            if (response.isSuccessful && response.body() != null) {
                val reservationResponse = response.body()!!

                Result.success(reservationResponse)
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

    override suspend fun getAllReservations(): Result<List<Reservation>> {
        return try {
            val response = apiService.getAllReservations()

            if (response.isSuccessful && response.body() != null) {
                val reservationsResponse = response.body()!!
                val reservations = reservationsResponse.map { it.toDomain() }
                Result.success(reservations)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "No autorizado"
                    403 -> "Sin permisos"
                    else -> "Error inesperado (${response.code()})"
                }
                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun editReservation(
        id: Long,
        request: ReservationRequest
    ): Result<ReservationResponse> {
        return try {
            val response = apiService.editReservation(id, request)
            if (response.isSuccessful && response.body() != null) {
                val reservationResponse = response.body()!!
                Result.success(reservationResponse)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "No autorizado"
                    403 -> "Sin permisos"
                    else -> "Error inesperado (${response.code()})"
                }
                Result.failure(Exception("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}