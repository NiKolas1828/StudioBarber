package com.dsmovil.studiobarber.ui.components.admin

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.domain.models.Service

@Composable
fun ServiceDialog(
    serviceToEdit: Service? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, price: String) -> Unit
) {
    // Estados del formulario
    var name by remember { mutableStateOf(serviceToEdit?.name ?: "") }
    var description by remember { mutableStateOf(serviceToEdit?.description ?: "") }
    var price by remember { mutableStateOf(serviceToEdit?.price ?: "") }

    val isEditMode = serviceToEdit != null
    val dialogTitle = if (isEditMode) "Editar Servicio" else "Nuevo Servicio"

    AdminDialogLayout(
        title = dialogTitle,
        onConfirm = { onConfirm(name, description, price.toString()) },
        onDismiss = onDismiss,
        colorSaveButton = colorResource(id = R.color.icon_color_blue)
    ) {
        AdminTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nombre",
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(8.dp))

        AdminTextField(
            value = description,
            onValueChange = { description = it },
            label = "Descripción",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(8.dp))

        AdminTextField(
            value = price.toString(),
            onValueChange = { price = it },
            label = "Precio",
            keyboardType = KeyboardType.Phone
        )
    }
}