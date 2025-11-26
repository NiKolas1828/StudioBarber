package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.ui.screens.client.home.SelectOptions

@Composable
fun ReservationOptionSelector(
    selected: SelectOptions,
    onSelectedChange: (SelectOptions) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onSelectedChange(SelectOptions.SERVICIO) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == SelectOptions.SERVICIO) colorResource(id = R.color.icon_color_blue) else Color.Transparent,
                contentColor = if (selected == SelectOptions.SERVICIO) Color.White else colorResource(id = R.color.icon_color_blue)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.width(140.dp)
        ) {
            Text("Servicio")
        }

        Button(
            onClick = { onSelectedChange(SelectOptions.BARBERO) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == SelectOptions.BARBERO) colorResource(id = R.color.icon_color_blue) else Color.Transparent,
                contentColor = if (selected == SelectOptions.BARBERO) Color.White else colorResource(id = R.color.icon_color_blue)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.width(140.dp)
        ) {
            Text("Barbero")
        }
    }
}