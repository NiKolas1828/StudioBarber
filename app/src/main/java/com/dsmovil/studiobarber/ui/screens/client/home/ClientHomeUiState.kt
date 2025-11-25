package com.dsmovil.studiobarber.ui.screens.client.home

import com.dsmovil.studiobarber.domain.models.Barber


data class ClientHomeUiState (
    val userName: String = "Usuario",

    val barbersState: BarbersDataState = BarbersDataState.Loading,
    val servicesState: ServicesDataState = ServicesDataState.Loading,

    val selectedBarberId: Long? = null,
    val selectedServiceId: Long? = null,
    val selectedOption: String = "barbero" // "barbero" o "servicio"
) {
    val isContinueButtonEnabled: Boolean
        get() = selectedBarberId != null && selectedServiceId != null

    sealed class BarbersDataState {
        data object Loading : BarbersDataState()
        data class Success(
            val barbers: List<Barber>
        ) : BarbersDataState()
        data class Error(
            val message: String
        ) : BarbersDataState()
    }

    sealed class ServicesDataState {
        data object Loading : ServicesDataState()
        data class Success(
            val services: List<Service>
        ) : ServicesDataState()
        data class Error(
            val message: String
        ) : ServicesDataState()
    }
}

