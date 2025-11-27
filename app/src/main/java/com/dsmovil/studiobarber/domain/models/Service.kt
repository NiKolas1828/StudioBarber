package com.dsmovil.studiobarber.domain.models

import java.util.Locale

data class Service (
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val isActive: Boolean = true
) {
    val type: ServiceType = determinateServiceType()

    private fun determinateServiceType(): ServiceType {
        val textToAnalyze = "$name $description".lowercase(Locale.ROOT)

        return when {
            textToAnalyze.contains("corte") || textToAnalyze.contains("cabello") -> ServiceType.HAIRCUT
            textToAnalyze.contains("barba") || textToAnalyze.contains("afeitado") -> ServiceType.BEARD
            textToAnalyze.contains("cejas") || textToAnalyze.contains("cara") -> ServiceType.EYEBROWS
            else -> ServiceType.OTHER
        }
    }
}

enum class ServiceType {
    HAIRCUT,
    BEARD,
    EYEBROWS,
    OTHER
}