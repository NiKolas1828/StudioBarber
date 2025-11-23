package com.dsmovil.studiobarber.ui.components.admin

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.domain.models.Barber

@Composable
fun BarberDialog(
    barberToEdit: Barber? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, email: String, phone: String, password: String) -> Unit
) {
    // Estados del formulario
    var name by remember { mutableStateOf(barberToEdit?.name ?: "") }
    var email by remember { mutableStateOf(barberToEdit?.email ?: "") }
    var phone by remember { mutableStateOf(barberToEdit?.phone ?: "") }
    var password by remember { mutableStateOf("") }

    val isEditMode = barberToEdit != null
    val dialogTitle = if (isEditMode) "Editar Barbero" else "Nuevo Barbero"

    AdminDialogLayout(
        title = dialogTitle,
        onConfirm = { onConfirm(name, email, phone, password) },
        onDismiss = onDismiss
    ) {
        BarberTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nombre Completo",
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(8.dp))

        BarberTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(8.dp))

        BarberTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Teléfono",
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(8.dp))

        BarberTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )

        if (isEditMode) {
            Text(
                text = "Deja el campo vacío para mantener la contraseña actual.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun BarberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorResource(id = R.color.background_color),
            unfocusedTextColor = colorResource(id = R.color.background_color),
            focusedBorderColor = colorResource(id = R.color.icon_color_red),
            focusedLabelColor = colorResource(id = R.color.icon_color_red),
            cursorColor = colorResource(id = R.color.background_color)
        )
    )
}