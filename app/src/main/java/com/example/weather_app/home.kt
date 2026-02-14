package com.example.weather_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            TopBar()
            Spacer(modifier = Modifier.height(32.dp))
            CurrentWeather()
            Spacer(modifier = Modifier.height(24.dp))
            WeatherDetailsGrid()
            Spacer(modifier = Modifier.height(24.dp))
            HourlyForecast()
            Spacer(modifier = Modifier.height(24.dp))
            DailyForecast()
            Spacer(modifier = Modifier.height(80.dp))
        }

    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cairo",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun CurrentWeather() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherIcon(condition = "Light Rain")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "27°C",
            color = Color.White,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Light Rain",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Feels Like: 25°C",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Text(
            text = "Sat, Feb 14 • 10:46 PM",
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
        when (condition) {
            "Light Rain" -> RainIcon()
            "Light Snow" -> SnowIcon()
            "Clear Sky" -> SunIcon()
            "Scattered Clouds" -> CloudIcon()
            else -> CloudIcon()
        }
    }
}

@Composable
fun RainIcon() {
    Icon(
        imageVector = Icons.Default.FavoriteBorder,
        contentDescription = null,
        tint = Color(0xFF4FC3F7),
        modifier = Modifier.size(120.dp)
    )
}

@Composable
fun SnowIcon() {
    Icon(
        imageVector = Icons.Default.FavoriteBorder,
        contentDescription = null,
        tint = Color(0xFF81D4FA),
        modifier = Modifier.size(120.dp)
    )
}

@Composable
fun SunIcon() {
    Icon(
        imageVector = Icons.Default.FavoriteBorder,
        contentDescription = null,
        tint = Color(0xFFFDD835),
        modifier = Modifier.size(120.dp)
    )
}

@Composable
fun CloudIcon() {
    Icon(
        imageVector = Icons.Default.FavoriteBorder ,
        contentDescription = null,

        tint = Color.White,
        modifier = Modifier.size(120.dp)
    )
}

@Composable
fun WeatherDetailsGrid() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeatherDetailCard(
                icon = Icons.Default.FavoriteBorder,
                title = "Humidity",
                value = "67%",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailCard(
                icon = Icons.Default.FavoriteBorder,
                title = "Wind Speed",
                value = "6 m/s",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeatherDetailCard(
                icon = Icons.Default.FavoriteBorder,
                title = "Pressure",
                value = "1025 hPa",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailCard(
                icon = Icons.Default.FavoriteBorder,
                title = "Clouds",
                value = "90%",
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
            containerColor = Color.White.copy(alpha = 0.1f)
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

@Composable
fun HourlyForecast() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Hourly Forecast",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        val hourlyData = listOf(
            HourlyData("10 PM", "29°C", "Scattered Clouds"),
            HourlyData("11 PM", "29°C", "Scattered Clouds"),
            HourlyData("12 AM", "25°C", "Scattered Clouds"),
            HourlyData("01 AM", "24°C", "Clear Sky"),
            HourlyData("02 AM", "23°C", "Clear Sky")
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(hourlyData) { data ->
                HourlyForecastCard(data)
            }
        }
    }
}

@Composable
fun HourlyForecastCard(data: HourlyData) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = data.time,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )

            Icon(
                imageVector = when (data.condition) {
                    "Clear Sky" -> Icons.Default.FavoriteBorder
                    "Scattered Clouds" -> Icons.Default.FavoriteBorder
                    else -> Icons.Default.FavoriteBorder
                },
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = data.temperature,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun DailyForecast() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "5-Day Forecast",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        val dailyData = listOf(
            DailyData("Sat, Feb 14", "Light Snow", "34°C", "26°C", "Light Snow"),
            DailyData("Sun, Feb 15", "Light Snow", "36°C", "23°C", "Light Snow"),
            DailyData("Mon, Feb 16", "Scattered Clouds", "34°C", "27°C", "Scattered Clouds"),
            DailyData("Tue, Feb 17", "Clear Sky", "35°C", "25°C", "Clear Sky"),
            DailyData("Wed, Feb 18", "Scattered Clouds", "36°C", "24°C", "Scattered Clouds")
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            dailyData.forEach { data ->
                DailyForecastCard(data)
            }
        }
    }
}

@Composable
fun DailyForecastCard(data: DailyData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.date,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = data.description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = when (data.condition) {
                    "Light Snow" -> Icons.Default.FavoriteBorder
                    "Clear Sky" -> Icons.Default.FavoriteBorder
                    "Scattered Clouds" -> Icons.Default.FavoriteBorder
                    else -> Icons.Default.FavoriteBorder
                },
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = data.maxTemp,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = data.minTemp,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }
    }
}


data class HourlyData(
    val time: String,
    val temperature: String,
    val condition: String
)

data class DailyData(
    val date: String,
    val description: String,
    val maxTemp: String,
    val minTemp: String,
    val condition: String
)