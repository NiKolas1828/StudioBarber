package com.dsmovil.studiobarber.ui.components.client.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HoursSelector(
    hours: List<HourItem>,
    selectedHour: String?,
    onSelectHour: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        hours.chunked(3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { hour ->

                    val bg = when {
                        hour.isAvailable.not() -> Color(0xFFE53935)
                        selectedHour == hour.value -> Color(0xFF03A9F4)
                        else -> Color(0xFF0288D1)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                            .background(bg, RoundedCornerShape(8.dp))
                            .clickable(enabled = hour.isAvailable) {
                                onSelectHour(hour.value)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hour.value,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

data class HourItem(
    val value: String,
    val isAvailable: Boolean
)