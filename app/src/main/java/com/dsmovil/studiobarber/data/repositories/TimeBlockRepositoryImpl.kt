package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.remote.timeblock.TimeBlockApiService
import com.dsmovil.studiobarber.domain.repositories.TimeBlockRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeBlockRepositoryImpl @Inject constructor(
        private val apiService: TimeBlockApiService
) : TimeBlockRepository {

    override suspend fun verifyAvailability(
            barberId: Long,
            date: LocalDate,
            startTime: LocalTime,
            endTime: LocalTime
    ): Result<Boolean> {
        return try {
            val response = apiService.verifyAvailability(
                    idBarber = barberId,
                    date = date.toString(),
                    startTime = startTime.format(DateTimeFormatter.ISO_LOCAL_TIME),
                    endTime = endTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = "Error al verificar disponibilidad: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
