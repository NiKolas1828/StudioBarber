package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateReservationScreenLayout(
    userName: String,
    selectedOption: String,
    onChangeOption: (String) -> Unit,
    isContinueEnabled: Boolean,
    onContinueClick: () -> Unit,
    onNavigateToHeader: () -> Unit = {},
    onLogout: () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF1E1E1E),
        snackbarHost = snackbarHost
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 140.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    HomeHeader(
                        userName = userName,
                        onMyReservationsClick = onNavigateToHeader
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HomeOptionsSelector(
                        selected = selectedOption,
                        onSelectedChange = onChangeOption
                    )
                    content()
                }

                BottomActionBar(
                    enabled = isContinueEnabled,
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

