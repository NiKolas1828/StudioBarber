package com.dsmovil.studiobarber.ui.screens.client.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.ui.components.CustomSnackbarHost
import com.dsmovil.studiobarber.ui.components.LogoutButton
import com.dsmovil.studiobarber.ui.components.ReservationHeader
import com.dsmovil.studiobarber.ui.components.client.selector.DaysSelector
import com.dsmovil.studiobarber.ui.components.client.selector.HoursSelector
import com.dsmovil.studiobarber.ui.components.client.ReserveButton
import kotlinx.coroutines.delay // Importante para la espera antes de navegar
import kotlinx.coroutines.launch // Importante para lanzar el snackbar

@Composable
fun ClientCalendarScreen(
    viewModel: ClientCalendarViewModel,
    onMyReservationsClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            containerColor = colorResource(R.color.background_color),
            snackbarHost = {
                CustomSnackbarHost(hostState = snackbarHostState)
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    ReservationHeader {
                        Button(
                            onClick = onMyReservationsClick,
                            modifier = Modifier
                                .width(150.dp)
                                .height(45.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.icon_color_blue),
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Text("Mis reservas")
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = onNavigateBack,
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.icon_color_red),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .width(60.dp)
                            .height(30.dp)
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = state.selectedMonth,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    DaysSelector(
                        selectedDate = state.selectedDate,
                        days = state.days,
                        onSelectDay = { viewModel.selectDate(it) },
                        onVisibleDayChanged = { viewModel.onVisibleDayChanged(it) }
                    )

                    Spacer(Modifier.height(30.dp))

                    Text(
                        text = "Horarios disponibles",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(15.dp))

                    HourFormatSelector(
                        isAm = state.isAm,
                        onChange = viewModel::toggleAmPm
                    )

                    Spacer(Modifier.height(15.dp))

                    HoursSelector(
                        hours = state.hours,
                        selectedHour = state.selectedHour,
                        onSelectHour = { viewModel.selectHour(it) }
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    ReserveButton(
                        enabled = !state.isLoading && state.selectedHour != null && state.selectedDate != null,
                        onClick = {
                            viewModel.confirmAction(
                                onSuccess = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("¡Reserva creada con éxito!")
                                        onMyReservationsClick()
                                    }
                                },
                                onError = { mensajeError ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = mensajeError,
                                            actionLabel = "ERROR_TYPE",
                                            withDismissAction = true
                                        )
                                    }
                                }
                            )
                        }
                    )

                    Spacer(Modifier.height(20.dp))

                    LogoutButton(
                        onClick = onLogout,
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@Composable
fun HourFormatSelector(
    isAm: Boolean,
    onChange: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = onChange,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03A9F4)
            )
        ) {
            Text(if (isAm) "AM ↓" else "PM ↓")
        }
    }
}