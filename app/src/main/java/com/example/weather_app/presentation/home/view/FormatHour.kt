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
fun formatToHour(dateTime: String?, context: Context): String {
    if (dateTime.isNullOrEmpty()) return ""

    val savedLang = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        .getString("language_code", "en") ?: "en"
    val locale = if (savedLang == "ar") Locale("ar") else Locale.ENGLISH

    val hour = dateTime.substring(11, 13).toInt()
    val minute = dateTime.substring(14, 16).toInt()
    val amPm = if (hour >= 12) "PM" else "AM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    return if (savedLang == "ar") {
        val arabicAmPm = if (hour >= 12) "م" else "ص"
        String.format(locale, "%d:%02d %s", displayHour, minute, arabicAmPm)
    } else {
        String.format("%02d:%02d %s", displayHour, minute, amPm)
    }
}

