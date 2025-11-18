package com.dsmovil.studiobarber.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.ui.components.BarberCard

@Composable
fun HomeScreen(
    userName: String = "Usuario", // Parámetro para el nombre del usuario
    onMyReservationsClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    // Estado para manejar qué barbero está seleccionado
    var selectedBarberId by remember { mutableStateOf<Int?>(null) }

    // Lista de barberos (esto debería venir de un ViewModel)
    val barbers = remember {
        listOf(
            Barber(1, "Carlos Martínez"),
            Barber(2, "Juan Pérez"),
            Barber(3, "Pedro García"),
            Barber(4, "Luis Rodríguez")
        )
    }

    Scaffold(
        containerColor = Color(0xFF1E1E1E), // Fondo oscuro
        bottomBar = {
            // Botón continuar fijo al fondo
            BottomActionBar(
                enabled = selectedBarberId != null,
                onClick = onContinueClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HomeHeader(
                    userName = userName,
                    onMyReservationsClick = onMyReservationsClick
                )
            }

            // Sección de servicio
            item {
                ServiceSection()
            }

            // Lista de barberos
            item {
                Text(
                    text = "Selecciona tu barbero",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(barbers) { barber ->
                BarberCard(
                    name = barber.name,
                    selected = selectedBarberId == barber.id,
                    onClick = {
                        selectedBarberId = barber.id
                    }
                )
            }

            // Espaciado final
            item {
                Spacer(modifier = Modifier.height(16.dp))
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
                fontSize = 14.sp
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
private fun ServiceSection() {
    Column {
        Text(
            text = "Servicio",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Button(
            onClick = { /* Cambiar servicio */ },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2C2C2C),
                contentColor = Color(0xFF03A9F4)
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Corte de cabello",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun BottomActionBar(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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

// Data class para representar un barbero
data class Barber(
    val id: Int,
    val name: String
)