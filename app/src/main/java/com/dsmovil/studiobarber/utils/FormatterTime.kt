package com.dsmovil.studiobarber.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatTimeForDisplay(time: LocalTime, showAmPm: Boolean = true): String {
    val format = if (showAmPm) "h:mm a" else "HH:mm"
    val formatter = DateTimeFormatter.ofPattern(format, Locale.getDefault())

    return time.format(formatter)
}

fun convertTo24HourFormat(hour12: String, isAm: Boolean): String {
    val parts = hour12.split(":")
    var hour = parts[0].toInt()
    val minute = parts[1]

    if (isAm) {
        if (hour == 12) hour = 0
    } else {
        if (hour != 12) hour += 12
    }

    return String.format(Locale.getDefault(), "%02d:%s", hour, minute)
}