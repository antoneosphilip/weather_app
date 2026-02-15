package com.example.weather_app.presentation.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.example.weather_app.presentation.home.viewModel.HomeViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1D2E))
    ) {
        when {
            viewModel.isLoading.value -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
            viewModel.error.value.isNotEmpty() -> {
                ErrorMessage(
                    error = viewModel.error.value,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                viewModel.weatherResponse.value?.let { weather ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        TopBar(cityName = weather.name)
                        Spacer(modifier = Modifier.height(32.dp))
                        CurrentWeather(
                            temperature = weather.main.temp.roundToInt(),
                            condition = weather.weather.firstOrNull()?.description ?: "",
                            feelsLike = weather.main.feelsLike.roundToInt(),
                            timestamp = weather.dt
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        WeatherDetailsGrid(
                            humidity = weather.main.humidity,
                            windSpeed = weather.wind.speed,
                            pressure = weather.main.pressure,
                            clouds = weather.clouds.all
                        )
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(error: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = error,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TopBar(cityName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = cityName,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun CurrentWeather(
    temperature: Int,
    condition: String,
    feelsLike: Int,
    timestamp: Long
) {
    val dateFormat = SimpleDateFormat("EEE, MMM dd • hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(timestamp * 1000))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherIcon(condition = condition)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$temperature°C",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = condition.replaceFirstChar { it.titlecase() },
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Feels Like: $feelsLike°C",
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

@Composable
fun WeatherDetailsGrid(
    humidity: Int,
    windSpeed: Double,
    pressure: Int,
    clouds: Int
) {
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
                title = "Humidity",
                value = "$humidity%",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailCard(
                icon = Icons.Default.LocationOn,
                title = "Wind Speed",
                value = "${windSpeed.roundToInt()} m/s",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeatherDetailCard(
                icon = Icons.Default.LocationOn,
                title = "Pressure",
                value = "$pressure hPa",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailCard(
                icon = Icons.Default.LocationOn,
                title = "Clouds",
                value = "$clouds%",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun WeatherDetailCard(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF252836)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            Text(
                text = value,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}