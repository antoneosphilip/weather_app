package com.example.weather_app.presentation.home.view

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatToDate(timestamp: Long, context: Context): String {
    val savedLang = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        .getString("language_code", "en") ?: "en"
    val locale = if (savedLang == "ar") Locale("ar") else Locale.ENGLISH
    val pattern = if (savedLang == "ar") "EEEE، d MMM" else "EEE, MMM d"
    val instant = Instant.ofEpochSecond(timestamp)
    val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return date.format(formatter)
}