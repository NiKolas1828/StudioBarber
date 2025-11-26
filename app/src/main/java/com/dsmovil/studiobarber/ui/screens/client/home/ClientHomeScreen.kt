package com.dsmovil.studiobarber.ui.screens.client.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.ui.components.CreateReservationScreenLayout
import com.dsmovil.studiobarber.ui.components.client.BarberCard
import com.dsmovil.studiobarber.ui.components.client.ServiceCard
import com.dsmovil.studiobarber.ui.components.utils.getIconForServiceType

@Composable
fun ClientHomeScreen(
    viewModel: ClientHomeViewModel,
    userName: String = "Usuario",
    onNavigateToClientReservarionts: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onLogout: () -> Unit ={}
) {
    val state by viewModel.uiState.collectAsState()

    CreateReservationScreenLayout(
        userName = userName,
        selectedOption = state.selectedOption,
        onChangeOption = viewModel::changeOption,
        isContinueEnabled = state.isContinueButtonEnabled,
        onContinueClick = onContinueClick,
        onNavigateToReservations = onNavigateToClientReservarionts,
        onLogout = onLogout
    ) {
        ClientHomeContent(
            contentState = state,
            viewModel = viewModel
        )
    }
}

@Composable
private fun ClientHomeContent(
    contentState: ClientHomeUiState,
    viewModel: ClientHomeViewModel
){
    when(contentState.selectedOption){
        "barbero" -> {
            Title("Selecciona tu barbero")

            when (val barberState = contentState.barbersState) {
                is ClientHomeUiState.BarbersDataState.Loading ->
                    LoadingShimmer()

                is ClientHomeUiState.BarbersDataState.Error ->
                    ErrorMessage(barberState.message)

                is ClientHomeUiState.BarbersDataState.Success ->
                    BarberList(
                        selected = contentState.selectedBarberId,
                        barbers = barberState.barbers,
                        viewModel = viewModel
                    )
            }
        }

        "servicio" -> {
            Title("Selecciona el servicio")

            when (val serviceState = contentState.servicesState) {
                is ClientHomeUiState.ServicesDataState.Loading ->
                    LoadingShimmer()

                is ClientHomeUiState.ServicesDataState.Error ->
                    ErrorMessage(serviceState.message)

                is ClientHomeUiState.ServicesDataState.Success -> {
                    ServiceList(
                        selected = contentState.selectedServiceId,
                        services = serviceState.services,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun BarberList(
    selected: Long?,
    barbers: List<Barber>,
    viewModel: ClientHomeViewModel
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(barbers) { barber ->
            BarberCard(
                name = barber.name,
                selected = selected == barber.id,
                onClick = { viewModel.selectBarber(barber.id) }
            )
        }
    }
}

@Composable
private fun ServiceList(
    selected: Long?,
    services: List<Service>,
    viewModel: ClientHomeViewModel
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(services) { service ->
            ServiceCard (
                name = service.name,
                description = service.description,
                icon = getIconForServiceType(service.type),
                selected = selected == service.id,
                onClick = { viewModel.selectService(service.id) }
            )
        }
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun LoadingShimmer() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
}