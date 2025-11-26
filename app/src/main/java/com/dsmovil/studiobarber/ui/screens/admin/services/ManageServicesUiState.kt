package com.dsmovil.studiobarber.ui.screens.admin.services

import com.dsmovil.studiobarber.domain.models.Service

sealed class ManageServicesUiState {
    data object Loading : ManageServicesUiState()
    data class Success(
        val services: List<Service>
    ) : ManageServicesUiState()
    data class Error(
        val message: String
    ) : ManageServicesUiState()
}