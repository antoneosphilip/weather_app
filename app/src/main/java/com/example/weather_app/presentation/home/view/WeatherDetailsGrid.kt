package com.example.weather_app.presentation.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weather_app.R

@Composable
fun WeatherDetailsGrid(
    humidity: Int,
    windSpeed: Double,
    pressure: Int,
    clouds: Int,
    windSpeedUnit: String
) {
    val context = LocalContext.current
    val convertedSpeed = if (windSpeedUnit == "mile"||windSpeedUnit==stringResource(R.string.unit_mph))
        (windSpeed * 2.237).roundToInt()
    else
        windSpeed.roundToInt()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeatherDetailCard(
                icon = Icons.Default.LocationOn,
                title = stringResource(R.string.humidity),
                value = "${formatNumber(humidity, context)}%",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailCard(
                icon = Icons.Default.LocationOn,
                title = stringResource(R.string.wind_speed),
                value = "${formatNumber(convertedSpeed, context)} $windSpeedUnit",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeatherDetailCard(
                icon = Icons.Default.LocationOn,
                title = stringResource(R.string.pressure),
                value = "${formatNumber(pressure, context)} ${stringResource(R.string.unit_hpa)  }",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailCard(
                icon = Icons.Default.LocationOn,
                title = stringResource(R.string.clouds),
                value = "${formatNumber(clouds, context)}%",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

