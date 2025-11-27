package com.dsmovil.studiobarber.ui.screens.admin.reservations

import com.dsmovil.studiobarber.domain.models.Reservation

sealed class AdminReservationsUiState {
    data object Loading : AdminReservationsUiState()
    data class Success(
        val reservations: List<Reservation>
    ) : AdminReservationsUiState()
    data class Error(
        val message: String
    ) : AdminReservationsUiState()
}