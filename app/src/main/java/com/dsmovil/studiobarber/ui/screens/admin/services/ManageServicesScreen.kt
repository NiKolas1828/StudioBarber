package com.dsmovil.studiobarber.ui.screens.admin.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.domain.models.Service
import com.dsmovil.studiobarber.ui.components.CustomSnackbarHost
import com.dsmovil.studiobarber.ui.components.admin.AddButtonFab
import com.dsmovil.studiobarber.ui.components.admin.AdminItemCard
import com.dsmovil.studiobarber.ui.components.admin.AdminScreenLayout
import com.dsmovil.studiobarber.ui.components.admin.services.ServiceDialog
import com.dsmovil.studiobarber.ui.components.utils.getIconForServiceType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ManageServicesScreen(
    viewModel: ManageServicesViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val selectedService by viewModel.selectedService.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ServiceSnackbar(viewModel = viewModel, snackbarHostState = snackbarHostState)

    if (showDialog) {
        ServiceDialog(
            serviceToEdit = selectedService,
            onDismiss = viewModel::closeDialog,
            onConfirm = viewModel::onConfirmDialog
        )
    }

    AdminScreenLayout(
        viewModel = viewModel,
        onLogoutSuccess = onLogout,
        snackbarHost = { CustomSnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            AddButtonFab(onClick = viewModel::openAddDialog)
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ManageServicesHeader(onNavigateBack)

            Spacer(modifier = Modifier.height(32.dp))

            ManageServicesContent(
                uiState = uiState,
                onEditClick = viewModel::openEditDialog,
                onDeleteClick = viewModel::deleteService,
                onRetry = viewModel::clearError
            )
        }
    }
}

@Composable
private fun ManageServicesHeader(onNavigateBack: () -> Unit) {
    Spacer(modifier = Modifier.height(24.dp))
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onNavigateBack,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.icon_color_blue),
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.align(Alignment.CenterStart).width(60.dp).height(30.dp)
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver"
            )
        }
        Text(
            text = "Servicios",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ManageServicesContent(
    uiState: ManageServicesUiState,
    onEditClick: (Service) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is ManageServicesUiState.Loading -> {
            LoadingView()
        }
        is ManageServicesUiState.Error -> {
            ErrorView(uiState.message, onRetry)
        }
        is ManageServicesUiState.Success -> {
            ServicesList(
                services = uiState.services,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
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
private fun ServicesList(
    services: List<Service>,
    onEditClick: (Service) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items = services, key = { it.id }) { service ->
            AdminItemCard(
                icon = getIconForServiceType(service.type),
                iconColor = colorResource(id = R.color.icon_color_red),
                iconBackgroundColor = Color.Transparent,
                onEditClick = { onEditClick(service) },
                onDeleteClick = { onDeleteClick(service.id) },
                textContent = {
                    Text(
                        text = service.name,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Descripción: ${service.description}",
                        lineHeight = 16.sp,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Precio: $${service.price}",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
private fun ServiceSnackbar(
    viewModel: ManageServicesViewModel,
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