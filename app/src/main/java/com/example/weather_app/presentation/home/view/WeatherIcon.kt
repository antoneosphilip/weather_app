package com.example.weather_app.presentation.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WeatherIcon(condition: String) {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            condition.contains("rain", ignoreCase = true) -> RainIcon()
            condition.contains("snow", ignoreCase = true) -> SnowIcon()
            condition.contains("clear", ignoreCase = true) -> SunIcon()
            condition.contains("cloud", ignoreCase = true) -> CloudIcon()
            else -> CloudIcon()
        }
    }
}


@Composable
fun RainIcon() {
    Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        tint = Color(0xFF4FC3F7),
        modifier = Modifier.size(120.dp)
    )
}

@Composable
fun SnowIcon() {
    Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        tint = Color(0xFF81D4FA),
        modifier = Modifier.size(120.dp)
    )
}

@Composable
fun SunIcon() {
    Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        tint = Color(0xFFFDD835),
        modifier = Modifier.size(120.dp)
    )
}


@Composable
fun CloudIcon() {
    Icon(
        imageVector = Icons.Default.LocationOn,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(120.dp)
    )
}