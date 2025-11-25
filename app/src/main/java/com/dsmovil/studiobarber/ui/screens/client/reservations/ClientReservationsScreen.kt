package com.dsmovil.studiobarber.ui.screens.client.reservations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.ui.components.Footer
import com.dsmovil.studiobarber.ui.components.LogoutButton
import com.dsmovil.studiobarber.ui.components.ReservationCard
import com.dsmovil.studiobarber.ui.components.client.ClientScreenLayout
import com.dsmovil.studiobarber.ui.screens.client.home.ErrorMessage
import com.dsmovil.studiobarber.ui.screens.client.home.LoadingShimmer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment


@Composable
fun ClientReservationsScreen(
    viewModel: ClientReservationViewModel,
    userName: String = "Usuario",
    onNavigateToClientHome: () -> Unit = {},
    onLogout: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    ClientScreenLayout{
        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
                .align(Alignment.TopStart)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                ClientReservationsHeader(
                    userName = userName,
                    onMyReservationsClick = onNavigateToClientHome
                )

                Spacer(modifier = Modifier.height(16.dp))

                ClientReservationsContent(
                    reservationState = state.reservationState,
                    viewModel = viewModel
                )
            }

            Footer(
                message = "Gracias por preferirnos",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                actions = { LogoutButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start)
                )},
            )
        }
    }
}

@Composable
private fun ClientReservationsHeader(
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
                containerColor = Color(0xFFF44336),
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            )
        ) {
            Text("Inicio")
        }
    }
}

@Composable
private fun ClientReservationsContent(
    reservationState: ClientReservationUiState.ReservationDataState,
    viewModel: ClientReservationViewModel
) {
    when (reservationState) {
        is ClientReservationUiState.ReservationDataState.Loading -> {
            LoadingShimmer()
        }

        is ClientReservationUiState.ReservationDataState.Error -> {
            ErrorMessage(reservationState.message)
        }

        is ClientReservationUiState.ReservationDataState.Success -> {
            ReservationsList(
                reservations = reservationState.reservations,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ReservationsList(
    reservations: List<Reservation>,
    viewModel: ClientReservationViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(reservations) { reservation ->
            ReservationCard(
                onDeleteClick = { viewModel.deleteReservation(reservation.id) },
                reservation = reservation
            )
        }
    }
}

