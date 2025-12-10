package com.dsmovil.studiobarber.domain.repositories

import com.dsmovil.studiobarber.domain.models.Barber

interface BarberRepository {
    suspend fun getBarbers(): Result<List<Barber>>
    suspend fun deleteBarber(id: Long): Result<Unit>
    suspend fun addBarber(barber: Barber): Result<Unit>
    suspend fun updateBarber(barber: Barber): Result<Unit>
}