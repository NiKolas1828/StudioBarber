package com.dsmovil.studiobarber.ui.screens.admin.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.ui.components.admin.AdminDashboardCard
import com.dsmovil.studiobarber.ui.components.admin.AdminScreenLayout

@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel,
    onNavigateToServices: () -> Unit,
    onNavigateToBarbers: () -> Unit,
    onNavigateToReservations: () -> Unit,
    onLogout: () -> Unit
) {
    AdminScreenLayout(
        viewModel = viewModel,
        onLogoutSuccess = onLogout
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            AdminHeader()

            Spacer(modifier = Modifier.height(32.dp))

            AdminToolsSection(
                onNavigateToServices = onNavigateToServices,
                onNavigateToBarbers = onNavigateToBarbers,
                onNavigateToReservations = onNavigateToReservations
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun AdminHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Bienvenido",
            color = Color.White,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Herramientas",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun AdminToolsSection(
    onNavigateToServices: () -> Unit,
    onNavigateToBarbers: () -> Unit,
    onNavigateToReservations: () -> Unit
) {
    Column {
        AdminDashboardCard(
            title = "Servicios",
            description = "Accede al listado de servicios en donde podrás eliminar o editar alguno de ellos",
            icon = ImageVector.vectorResource(id = R.drawable.ic_services),
            iconColor = colorResource(R.color.icon_color_blue),
            onClick = onNavigateToServices
        )

        Spacer(modifier = Modifier.height(16.dp))

        AdminDashboardCard(
            title = "Barberos",
            description = "Accede al listado de barberos en donde podrás eliminar o editar alguno de ellos",
            icon = ImageVector.vectorResource(id = R.drawable.ic_barber_pole),
            iconColor = colorResource(R.color.icon_color_red),
            onClick = onNavigateToBarbers
        )

        Spacer(modifier = Modifier.height(16.dp))

        AdminDashboardCard(
            title = "Reservas",
            description = "Accede al listado de reservas en donde podrás ver información detallada de cada una",
            icon = ImageVector.vectorResource(id = R.drawable.ic_reservations),
            iconColor = colorResource(R.color.icon_color_blue),
            onClick = onNavigateToReservations
        )
    }
}