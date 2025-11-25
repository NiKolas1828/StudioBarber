package com.dsmovil.studiobarber.domain.models

import java.time.LocalDate
import java.time.LocalTime

data class Reservation(
    val id: Long,
    val serviceId: Long,
    val nameService: String,
    val userId: Long,
    val nameUser: String,
    val barberId: Long,
    val nameBarber: String,
    val date: LocalDate,
    val timeStart: LocalTime,
    val status: Boolean,
    val isActive: Boolean = true,
    val amount: Boolean
)