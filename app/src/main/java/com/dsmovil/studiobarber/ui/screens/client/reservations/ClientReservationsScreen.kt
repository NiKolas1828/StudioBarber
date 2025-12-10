package com.dsmovil.studiobarber.ui.screens.client.reservations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.ui.components.ReservationCard
import com.dsmovil.studiobarber.ui.screens.client.home.ErrorMessage
import com.dsmovil.studiobarber.ui.screens.client.home.LoadingShimmer
import com.dsmovil.studiobarber.ui.components.ListReservationScreenLayout

@Composable
fun ClientReservationsScreen(
    viewModel: ClientReservationViewModel,
    onNavigateToClientHome: () -> Unit = {},
    onLogout: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    ListReservationScreenLayout(
        onNavigateToHome = onNavigateToClientHome,
        onLogout = onLogout,
        footerText = "Gracias por preferirnos"
    ){
        ClientReservationsContent(
            reservationState = state.reservationState,
            viewModel = viewModel
        )
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
    viewModel: ClientReservationViewModel
) {
    var reservationToDelete by remember { mutableStateOf<Reservation?>(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(reservations) { reservation ->
            ReservationCard(
                reservation = reservation,
                onEditClick = {},
                onDeleteClick = { reservationToDelete = reservation },
                userRole = Role.CLIENTE
            )
        }
    }

    if (reservationToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                reservationToDelete = null
            },
            title = {
                Text(text = "Cancelar Reserva")
            },
            text = {
                Text("¿Estás seguro de que deseas cancelar esta reserva? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        reservationToDelete?.let { reservation ->
                            viewModel.deleteReservation(reservation.id)
                        }
                        reservationToDelete = null
                    }
                ) {
                    Text("Sí, cancelar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        reservationToDelete = null
                    }
                ) {
                    Text("No, volver")
                }
            }
        )
    }
}

