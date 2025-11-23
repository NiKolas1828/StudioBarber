package com.dsmovil.studiobarber.ui.screens.admin.barbers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.ui.components.CustomSnackbarHost
import com.dsmovil.studiobarber.ui.components.admin.AdminItemCard
import com.dsmovil.studiobarber.ui.components.admin.AdminScreenLayout
import com.dsmovil.studiobarber.ui.components.admin.BarberDialog

@Composable
fun ManageBarbersScreen(
    viewModel: ManageBarbersViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val selectedBarber by viewModel.selectedBarber.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    barberSnackbar(viewModel = viewModel, snackbarHostState = snackbarHostState)

    if (showDialog) {
        BarberDialog(
            barberToEdit = selectedBarber,
            onDismiss = viewModel::closeDialog,
            onConfirm = { name, email, phone, password ->
                viewModel.onConfirmDialog(name, email, phone, password)
            }
        )
    }

    AdminScreenLayout(
        viewModel = viewModel,
        floatingActionButton = {
            AddBarberFab(modifier = Modifier.padding(bottom = 16.dp), onClick = viewModel::openAddDialog)
        },
        onLogoutSuccess = onLogout,
        snackbarHost = {
            CustomSnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ManageBarbersHeader(onNavigateBack = onNavigateBack)

            Spacer(modifier = Modifier.height(32.dp))

            ManageBarbersContent(
                uiState = uiState,
                onEditClick = viewModel::openEditDialog,
                onDeleteClick = viewModel::deleteBarber,
                onRetry = viewModel::clearError
            )
        }
    }
}

@Composable
private fun ManageBarbersHeader(onNavigateBack: () -> Unit) {
    Spacer(modifier = Modifier.height(24.dp))

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onNavigateBack,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.icon_color_red),
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(60.dp)
                .height(30.dp)
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver"
            )
        }

        Text(
            text = "Barberos",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ManageBarbersContent(
    uiState: ManageBarbersUiState,
    onEditClick: (Barber) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is ManageBarbersUiState.Loading -> LoadingView()
        is ManageBarbersUiState.Error -> ErrorView(message = uiState.message, onRetry = onRetry)
        is ManageBarbersUiState.Success -> SuccessView(
            barbers = uiState.barbers,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = colorResource(id = R.color.icon_color_blue))
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = Color.Red)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun SuccessView(
    barbers: List<Barber>,
    onEditClick: (Barber) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = barbers,
            key = { barber -> barber.id }
        ) { barber ->
            AdminItemCard(
                label = "Nombre",
                text = barber.name,
                icon = ImageVector.vectorResource(id = R.drawable.ic_user),
                iconBackgroundColor = Color.Transparent,
                onEditClick = { onEditClick(barber) },
                onDeleteClick = { onDeleteClick(barber.id) }
            )
        }

        if (barbers.isEmpty()) {
            item {
                Text(
                    text = "No hay barberos registrados.",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun AddBarberFab(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = Color.White,
            contentColor = colorResource(id = R.color.icon_color_blue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar Barbero",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun barberSnackbar(viewModel: ManageBarbersViewModel, snackbarHostState: SnackbarHostState) {
    LaunchedEffect(Unit) {
        viewModel.messageChannel.collect { uiMessage ->
            snackbarHostState.showSnackbar(
                message = uiMessage.message,
                actionLabel = if (uiMessage.isError) "ERROR_TYPE" else "SUCCESS_TYPE",
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }
    }
}