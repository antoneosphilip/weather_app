package com.example.weather_app.presentation.home.view

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun formatToHour(dateTime: String?): String {
    if (dateTime.isNullOrEmpty()) return ""

    val hour = dateTime.substring(11, 13).toInt()
    val amPm = if (hour >= 12) "PM" else "AM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return String.format("%02d %s", displayHour, amPm)
}