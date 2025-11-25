package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun Footer(
    modifier: Modifier = Modifier,
    message: String,
    actions: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        // Contenedor blanco del footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Texto principal
            Text(
                text = message,
                fontSize = 18.sp,
                color = Color.Black
            )

            // Slot para botones u otros elementos opcionales
            actions?.let {
                Spacer(modifier = Modifier.height(8.dp))
                it()
            }
        }
    }
}
