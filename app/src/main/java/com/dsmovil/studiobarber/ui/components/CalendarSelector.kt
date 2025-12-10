package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.ui.components.client.selector.DaysSelector
import com.dsmovil.studiobarber.ui.screens.client.calendar.DayItem

@Composable
fun CalendarSelector(
    selectedMonth: String,
    selectedDate: java.time.LocalDate,
    days: List<DayItem>,
    onSelectDay: (DayItem) -> Unit,
    onVisibleDayChanged: (DayItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = selectedMonth,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        DaysSelector(
            selectedDate = selectedDate,
            days = days,
            onSelectDay = onSelectDay,
            onVisibleDayChanged = onVisibleDayChanged
        )
    }
}
