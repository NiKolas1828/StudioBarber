package com.dsmovil.studiobarber.ui.screens.barber.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.ui.components.CalendarSelector
import com.dsmovil.studiobarber.ui.components.Footer
import com.dsmovil.studiobarber.ui.components.LogoutButton
import com.dsmovil.studiobarber.ui.components.ReservationCard

@Composable
fun BarberScheduleScreen(
    viewModel: BarberScheduleViewModel,
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = colorResource(R.color.background_color),

        bottomBar = {
            Footer(
                message = "Gracias por preferirnos",
                modifier = Modifier.fillMaxWidth()
            ) {
                LogoutButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Citas reservadas",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(20.dp))

            CalendarSelector(
                selectedMonth = state.selectedMonth,
                selectedDate = state.selectedDate,
                days = state.days,
                onSelectDay = { viewModel.selectDate(it) },
                onVisibleDayChanged = { viewModel.onVisibleDayChanged(it) }
            )

            // Contenedor de las reservas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (val result = state.reservationState) {

                    is BarberScheduleUiState.ReservationDataState.Loading -> {
                        CircularProgressIndicator(color = Color.White)
                    }

                    is BarberScheduleUiState.ReservationDataState.Error -> {
                        Text(text = result.message, color = Color.Red)
                    }

                    is BarberScheduleUiState.ReservationDataState.Success -> {
                        if (result.reservations.isEmpty()) {
                            Text(
                                text = "No hay reservas para este día",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(result.reservations) { reservation ->
                                    ReservationCard(
                                        reservation = reservation,
                                        onEditClick = null,
                                        onDeleteClick = null,
                                        userRole = Role.BARBERO
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
