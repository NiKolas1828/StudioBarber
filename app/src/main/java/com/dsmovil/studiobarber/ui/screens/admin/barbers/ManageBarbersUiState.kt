package com.dsmovil.studiobarber.ui.screens.admin.barbers

import com.dsmovil.studiobarber.domain.models.Barber

sealed class ManageBarbersUiState {
    data object Loading: ManageBarbersUiState()
    data class Success(
        val barbers: List<Barber>,
        val editingBarberId: Long? = null
    ) : ManageBarbersUiState()
    data class Error(
        val message: String
    ) : ManageBarbersUiState()
}