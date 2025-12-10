package com.dsmovil.studiobarber.ui.components.client.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.ui.screens.client.calendar.DayItem
import java.time.LocalDate
import com.dsmovil.studiobarber.R

@Composable
fun DaysSelector(
    selectedDate: LocalDate?,
    days: List<DayItem>,
    onSelectDay: (DayItem) -> Unit,
    onVisibleDayChanged: (DayItem) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (days.isNotEmpty() && index < days.size) {
                    onVisibleDayChanged(days[index])
                }
            }
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(days) { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onSelectDay(day) }
            ) {
                Text(
                    text = day.dayName,
                    fontSize = 13.sp,
                    color = Color.White.copy(.6f)
                )

                Spacer(Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(
                            color = if (selectedDate == day.date)
                                colorResource(id = R.color.icon_color_blue)
                            else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.dayNumber,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
