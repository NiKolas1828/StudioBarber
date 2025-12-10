package com.dsmovil.studiobarber.ui.screens.admin.reservations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.domain.models.Reservation
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.ui.components.CalendarSelector
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
        footerText = "Gestión de Reservas",
        snackbarHost = {
            CustomSnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            CalendarSelector(
                selectedMonth = uiState.selectedMonth,
                selectedDate = uiState.selectedDate,
                days = uiState.days,
                onSelectDay = viewModel::selectDate,
                onVisibleDayChanged = viewModel::onVisibleDayChanged,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReservationsContent(
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ReservationsContent(
    uiState: AdminReservationsUiState,
    viewModel: AdminReservationsViewModel
) {
    when (val state = uiState.reservationState) {
        is AdminReservationsUiState.ReservationDataState.Loading -> {
            LoadingShimmer()
        }

        is AdminReservationsUiState.ReservationDataState.Error -> {
            ErrorMessage(state.message)
        }

        is AdminReservationsUiState.ReservationDataState.Success -> {
            ReservationsList(
                reservations = state.reservations,
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
    if (reservations.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay reservas para este día",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(reservations) { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onDeleteClick = { viewModel.deleteReservation(reservation.id) },
                    userRole = Role.ADMINISTRADOR
                )
            }
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
                actionLabel = if (uiMessage.isError) "ERROR" else "OK",
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }
    }
}
