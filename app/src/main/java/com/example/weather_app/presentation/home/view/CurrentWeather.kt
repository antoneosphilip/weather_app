package com.example.weather_app.presentation.home.view

import WeatherIcon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentWeather(
    temperature: Int,
    condition: String,
    feelsLike: Int,
    timestamp: Long,
    icon:String,
    unit:String

) {
    val dateFormat = SimpleDateFormat("EEE, MMM dd • hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(timestamp * 1000))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherIcon(iconCode=icon, modifier = Modifier.size(120.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$temperature$unit",
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
            text = "Feels Like: $feelsLike$unit",
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