package com.dsmovil.studiobarber.ui.client.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.ui.components.BarberCard
import com.dsmovil.studiobarber.ui.components.ServiceCard

@Composable
fun ClientHomeScreen(
    userName: String = "Usuario",
    onNavigateToClientReservarionts: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    var selectedBarberId by remember { mutableStateOf<Int?>(null) }
    var selectedServiceId by remember { mutableStateOf<Int?>(null) }

    var selectOption by remember { mutableStateOf("barbero") }

    val barbers = remember {
        listOf(
            Barber(1, "Carlos Martínez"),
            Barber(2, "Juan Pérez"),
            Barber(3, "Pedro García"),
            Barber(4, "Luis Rodríguez")
        )
    }

    val services = remember {
        listOf(
            Service(1, "Corte de cabello", "Corte profesional"),
            Service(2, "Barba", "Arreglo de barba"),
            Service(3, "Cejas", "Perfilado de cejas")
        )
    }

    Scaffold(
        containerColor = Color(0xFF1E1E1E), // Fondo oscuro
        bottomBar = {
            BottomActionBar(
                enabled = (selectedBarberId != null && selectedServiceId != null),
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
                    onMyReservationsClick = onNavigateToClientReservarionts
                )
            }

            item {
                HomeOptionsSelector(
                    selected = selectOption,
                    onSelectedChange =  { selectOption = it }
                )
            }

            when (selectOption) {
                "barbero" -> {
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
                            onClick = { selectedBarberId = barber.id }
                        )
                    }
                }

                "servicio" -> {
                    item {
                        Text(
                            text = "Selecciona el servicio",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(services) { service ->
                        ServiceCard(
                            name = service.name,
                            description = service.description,
                            selected = selectedServiceId == service.id,
                            onClick = { selectedServiceId = service.id })
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

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
fun HomeOptionsSelector(
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
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

data class Barber(
    val id: Int,
    val name: String
)

data class Service(
    val id: Int,
    val name: String,
    val description: String
)