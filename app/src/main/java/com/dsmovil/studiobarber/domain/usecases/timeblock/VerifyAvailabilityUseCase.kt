package com.dsmovil.studiobarber.domain.usecases.timeblock

import com.dsmovil.studiobarber.domain.repositories.TimeBlockRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class VerifyAvailabilityUseCase @Inject constructor(
    private val repository: TimeBlockRepository
) {
    suspend operator fun invoke(
        barberId: Long,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): Result<Boolean> {
        return repository.verifyAvailability(barberId, date, startTime, endTime)
    }
}