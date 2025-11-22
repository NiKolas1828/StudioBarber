package com.dsmovil.studiobarber.ui.screens.admin.barbers

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.domain.models.Barber
import com.dsmovil.studiobarber.ui.components.admin.AdminEditableCard
import com.dsmovil.studiobarber.ui.components.admin.AdminItemCard
import com.dsmovil.studiobarber.ui.components.admin.AdminScreenLayout

@Composable
fun ManageBarbersScreen(
    viewModel: ManageBarbersViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AdminScreenLayout(
        viewModel = viewModel,
        floatingActionButton = {
            AddBarberFab(modifier = Modifier.padding(bottom = 60.dp), onClick = {})
        },
        onLogoutSuccess = onLogout
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ManageBarbersHeader(onNavigateBack = onNavigateBack)

            Spacer(modifier = Modifier.height(32.dp))

            ManageBarbersContent(
                uiState = uiState,
                onStartEdit = viewModel::onEditClick,
                onCancelEdit = viewModel::onCancelEdit,
                onSaveEdit = viewModel::onSaveEdit,
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
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .background(Color(0xFFF44336), shape = RoundedCornerShape(8.dp))
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
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
    onStartEdit: (Long) -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: (Barber, String) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is ManageBarbersUiState.Loading -> LoadingView()
        is ManageBarbersUiState.Error -> ErrorView(message = uiState.message, onRetry = onRetry)
        is ManageBarbersUiState.Success -> SuccessView(
            state = uiState,
            onStartEdit = onStartEdit,
            onCancelEdit = onCancelEdit,
            onSaveEdit = onSaveEdit,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF03A9F4))
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
    state: ManageBarbersUiState.Success,
    onStartEdit: (Long) -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: (Barber, String) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(
            items = state.barbers,
            key = { barber -> barber.id }
        ) { barber ->
            if (barber.id == state.editingBarberId) {
                AdminEditableCard(
                    initialName = barber.name,
                    onSave = { newName -> onSaveEdit(barber, newName) },
                    onCancel = onCancelEdit
                )
            } else {
                AdminItemCard(
                    label = "Nombre",
                    text = barber.name,
                    icon = Icons.Default.Person,
                    iconBackgroundColor = Color(0xFF03A9F4), // Azul Cian
                    onEditClick = { onStartEdit(barber.id) },
                    onDeleteClick = { onDeleteClick(barber.id) }
                )
            }
        }

        if (state.barbers.isEmpty()) {
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
            contentColor = Color(0xFF03A9F4),
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