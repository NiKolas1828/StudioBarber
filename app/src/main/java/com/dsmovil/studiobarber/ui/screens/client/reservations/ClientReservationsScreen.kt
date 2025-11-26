package com.dsmovil.studiobarber.ui.screens.client.reservations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.ui.components.ReservationCard
import com.dsmovil.studiobarber.ui.screens.client.home.ErrorMessage
import com.dsmovil.studiobarber.ui.screens.client.home.LoadingShimmer
import com.dsmovil.studiobarber.ui.components.ListReservationScreenLayout

@Composable
fun ClientReservationsScreen(
    viewModel: ClientReservationViewModel,
    userName: String = "Usuario",
    onNavigateToClientHome: () -> Unit = {},
    onLogout: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    ListReservationScreenLayout(
        userName = userName,
        onNavigateToHeader = onNavigateToClientHome,
        onLogout = onLogout,
        textFooter = "Gracias por preferirnos"
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

