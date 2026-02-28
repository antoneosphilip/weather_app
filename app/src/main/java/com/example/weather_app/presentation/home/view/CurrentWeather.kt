package com.example.weather_app.presentation.home.view

import WeatherIcon
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentWeather(
    temperature: Int,
    condition: String,
    feelsLike: Int,
    timestamp: Long,
    icon: String,
    unit: String
) {
    val context = LocalContext.current
    val savedLang = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        .getString("language_code", "en") ?: "en"
    val locale = if (savedLang == "ar") Locale("ar") else Locale.ENGLISH
    val pattern = if (savedLang == "ar") "EEE، d MMM • hh:mm a" else "EEE, MMM d • hh:mm a"
    val dateFormat = SimpleDateFormat(pattern, locale)
    val formattedDate = dateFormat.format(Date(timestamp * 1000))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherIcon(iconCode = icon, modifier = Modifier.size(120.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${formatNumber(temperature, context)}$unit",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = condition.replaceFirstChar { it.titlecase() },
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.feels_like, formatNumber(feelsLike, context), unit),
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
        Text(
            text = formattedDate,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}