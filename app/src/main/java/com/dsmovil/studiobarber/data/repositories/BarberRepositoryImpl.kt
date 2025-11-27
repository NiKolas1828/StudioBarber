package com.dsmovil.studiobarber.data.repositories

import com.dsmovil.studiobarber.data.local.MemoryDatabase // Asumimos esta clase ya existe
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.repositories.BarberRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarberRepositoryImpl @Inject constructor(
    private val db: MemoryDatabase
) : BarberRepository {


    override suspend fun getBarbers(): Result<List<Barber>> {
        delay(500)
        return Result.success(db.barbers.toList())
    }

    override suspend fun deleteBarber(id: Long): Result<Unit> {
        delay(300)
        val removed = db.barbers.removeIf { it.id == id }

        return if (removed) Result.success(Unit) else Result.failure(Exception("Barbero no encontrado"))
    }

    override suspend fun addBarber(barber: Barber): Result<Unit> {
        delay(300)
        db.barbers.add(barber)

        return Result.success(Unit)
    }

    override suspend fun getBarberById(id: Long): Result<Barber> {
        delay(300)
        val barber = db.barbers.find { it.id == id }

        return if (barber != null) {
            Result.success(barber)
        } else {
            Result.failure(Exception("Barbero no encontrado"))
        }
    }

    override suspend fun updateBarber(barber: Barber): Result<Unit> {
        delay(300)
        val index = db.barbers.indexOfFirst { it.id == barber.id }

        return if (index != -1) {
            db.barbers[index] = barber
            Result.success(Unit)
        } else {
            Result.failure(Exception("No se pudo actualizar"))
        }
    }
}