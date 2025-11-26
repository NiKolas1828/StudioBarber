package com.dsmovil.studiobarber.ui.screens.client.reservation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.ui.components.client.ClientScreenLayout
import com.dsmovil.studiobarber.ui.components.client.selector.DaysSelector
import com.dsmovil.studiobarber.ui.components.client.selector.HoursSelector
import com.dsmovil.studiobarber.ui.components.client.ReserveButton

@Composable
fun ClientReservationScreen(
    viewModel: ClientReservationDetailViewModel,
    onMyReservationsClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ClientScreenLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Bienvenido", color = Color.White.copy(.7f), fontSize = 22.sp)
                    Text(
                        state.userName,
                        color = Color(0xFF03A9F4),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Button(
                    onClick = onMyReservationsClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF03A9F4)
                    )
                ) {
                    Text("Mis reservas")
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = state.selectedMonth,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
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
                enabled = state.selectedHour != null && state.selectedDate != null,
                onClick = viewModel::reserve
            )

            Spacer(Modifier.height(20.dp))
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
