package com.example.weather_app.presentation.home.view

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun formatToDate(timestamp: Long): String {
    val instant = java.time.Instant.ofEpochSecond(timestamp)
    val date = java.time.LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
    val dayOfWeek = date.dayOfWeek.toString().substring(0, 3).replaceFirstChar { it.uppercase() }
    val month = date.month.toString().substring(0, 3).replaceFirstChar { it.uppercase() }
    val day = date.dayOfMonth
    return "$dayOfWeek, $month $day"
}