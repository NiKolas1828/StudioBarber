package com.dsmovil.studiobarber.ui.screens.admin.reservations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.ui.components.CustomSnackbarHost
import com.dsmovil.studiobarber.ui.components.ListReservationScreenLayout
import com.dsmovil.studiobarber.ui.components.ReservationCard
import com.dsmovil.studiobarber.ui.screens.client.home.ErrorMessage
import com.dsmovil.studiobarber.ui.screens.client.home.LoadingShimmer
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AdminReservationsScreen(
  viewModel: AdminReservationsViewModel,
  onNavigateToDashboard: () -> Unit,
  onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    ReservationSnackbar(viewModel = viewModel, snackbarHostState = snackbarHostState)

    ListReservationScreenLayout(
        onNavigateToHome = onNavigateToDashboard,
        onLogout = onLogout,
        footerText = "Gracias por preferirnos",
        snackbarHost = {
            CustomSnackbarHost(hostState = snackbarHostState)
        }
    ){
        ReservationsContent(
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

@Composable
private fun ReservationsContent(
    uiState: AdminReservationsUiState,
    viewModel: AdminReservationsViewModel
) {
    when (uiState) {
        is AdminReservationsUiState.Loading-> {
            LoadingShimmer()
        }

        is AdminReservationsUiState.Error -> {
            ErrorMessage(uiState.message)
        }

        is AdminReservationsUiState.Success -> {
            ReservationsList(
                reservations = uiState.reservations,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ReservationsList(
    reservations: List<Reservation>,
    viewModel: AdminReservationsViewModel
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(reservations) { reservation ->
            ReservationCard(
                reservation = reservation,
                onDeleteClick = { viewModel.deleteReservation(reservation.id) }
            )
        }
    }
}

@Composable
private fun ReservationSnackbar(
    viewModel: AdminReservationsViewModel,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        viewModel.messageChannel.collectLatest { uiMessage ->
            snackbarHostState.showSnackbar(
                message = uiMessage.message,
                actionLabel = if (uiMessage.isError) "ERROR_TYPE" else "SUCCESS_TYPE",
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }
    }
}