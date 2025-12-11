package com.dsmovil.studiobarber.domain.repositories

import java.time.LocalDate
import java.time.LocalTime

interface TimeBlockRepository {
    suspend fun verifyAvailability(barberId: Long,
                                   date: LocalDate,
                                   startTime: LocalTime,
                                   endTime: LocalTime
    ): Result<Boolean>
}
