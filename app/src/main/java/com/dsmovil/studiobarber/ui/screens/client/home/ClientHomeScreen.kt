package com.dsmovil.studiobarber.ui.screens.client.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.dsmovil.studiobarber.ui.components.LogoutButton
import com.dsmovil.studiobarber.ui.components.client.BarberCard
import com.dsmovil.studiobarber.ui.components.client.ClientScreenLayout
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


    ClientScreenLayout{
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 140.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                HomeHeader(
                    userName = userName,
                    onMyReservationsClick = onNavigateToClientReservarionts
                )

                Spacer(modifier = Modifier.height(16.dp))

                HomeOptionsSelector(
                    selected = state.selectedOption,
                    onSelectedChange =  viewModel::changeOption
                )

                ClientHomeContent(
                    contentState = state,
                    viewModel = viewModel
                )
            }

            BottomActionBar(
                enabled = state.isContinueButtonEnabled,
                onClick = onContinueClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp)
            )


            LogoutButton(
                onClick = onLogout,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            )
        }
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
private fun HomeHeader(
    userName: String,
    onMyReservationsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Bienvenido",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 25.sp
            )
            Text(
                text = userName,
                color = Color(0xFF03A9F4),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onMyReservationsClick,
            modifier = Modifier
                .width(150.dp)
                .height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03A9F4),
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            )
        ) {
            Text("Mis reservas")
        }
    }
}

@Composable
fun HomeOptionsSelector(
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Botón Servicio
        Button(
            onClick = { onSelectedChange("servicio") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == "servicio") Color(0xFF03A9F4) else Color.Transparent,
                contentColor = if (selected == "servicio") Color.White else Color(0xFF03A9F4)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.width(140.dp)
        ) {
            Text("Servicio")
        }

        // Botón Barbero
        Button(
            onClick = { onSelectedChange("barbero") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == "barbero") Color(0xFF03A9F4) else Color.Transparent,
                contentColor = if (selected == "barbero") Color.White else Color(0xFF03A9F4)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.width(140.dp)
        ) {
            Text("Barbero")
        }
    }
}


@Composable
private fun BottomActionBar(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF1E1E1E),
        tonalElevation = 8.dp
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF424242),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp
            )
        ) {
            Text(
                text = "Continuar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
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