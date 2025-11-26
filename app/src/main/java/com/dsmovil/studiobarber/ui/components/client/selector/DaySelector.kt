package com.dsmovil.studiobarber.ui.components.client.selector


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DaysSelector(
    selectedDay: Int?,
    onSelectDay: (Int) -> Unit,
    days: List<DayItem>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onSelectDay(day.dayNumber) }
            ) {

                Text(
                    text = day.shortName,
                    fontSize = 13.sp,
                    color = Color.White.copy(.6f)
                )

                Spacer(Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(
                            color = if (selectedDay == day.dayNumber)
                                Color(0xFF03A9F4)
                            else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.dayNumber.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class DayItem(
    val dayNumber: Int,
    val shortName: String
)
