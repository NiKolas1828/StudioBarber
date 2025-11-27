package com.dsmovil.studiobarber.ui.screens.client.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.ui.components.CreateReservationScreenLayout
import com.dsmovil.studiobarber.ui.components.SelectableItemCard
import com.dsmovil.studiobarber.ui.components.utils.getIconForServiceType

@Composable
fun ClientHomeScreen(
    viewModel: ClientHomeViewModel,
    onNavigateToClientReservarionts: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onLogout: () -> Unit ={}
) {
    val state by viewModel.uiState.collectAsState()

    CreateReservationScreenLayout(
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
        SelectOptions.BARBERO  -> {
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

        SelectOptions.SERVICIO -> {
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
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(barbers) { barber ->
            SelectableItemCard(
                selected = selected == barber.id,
                onClick = { viewModel.selectBarber(barber.id) },
                icon = ImageVector.vectorResource(id = R.drawable.ic_user),
                iconColor = colorResource(id = R.color.icon_color_blue),
                textContent = {
                    Text(
                        text = barber.name,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Barbero profesional",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
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
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(services) { service ->
            SelectableItemCard(
                selected = selected == service.id,
                onClick = { viewModel.selectService(service.id) },
                icon = getIconForServiceType(service.type),
                iconColor = colorResource(id = R.color.icon_color_red),
                textContent = {
                    Text(
                        text = service.name,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Descripción: ${service.description}",
                        lineHeight = 16.sp,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 3
                    )

                    Text(
                        text = "Precio: $${service.price}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
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