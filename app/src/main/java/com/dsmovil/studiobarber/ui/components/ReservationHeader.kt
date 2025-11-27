package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import com.dsmovil.studiobarber.R

@Composable
fun ReservationHeader(
    username: String = "Usuario",
    buttonHeader: @Composable () -> Unit
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
                text = username,
                color = colorResource(id = R.color.icon_color_blue),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        buttonHeader()
    }
}