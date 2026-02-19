package com.example.weather_app.presentation.home.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weather_app.Screens
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherComponent(
    weather: WeatherResponse,
    hourlyForecast: WeatherForecastResponse,
    dailyForecast: DailyForecastResponse,
    temperatureUnit: String,
    onLocationClick: () -> Unit
) {

    TopBar(cityName = weather.name)

    Spacer(modifier = Modifier.height(32.dp))

    Row(
        modifier = Modifier.clickable {
            onLocationClick()
        }
    ) {
        CurrentWeather(
            temperature = weather.main.temp.roundToInt(),
            icon = weather.weather.firstOrNull()?.icon ?: "",
            feelsLike = weather.main.feelsLike.roundToInt(),
            timestamp = weather.dt,
            condition = weather.weather.firstOrNull()?.description ?: "",
            unit = temperatureUnit
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    WeatherDetailsGrid(
        humidity = weather.main.humidity,
        windSpeed = weather.wind.speed,
        pressure = weather.main.pressure,
        clouds = weather.clouds.all
    )

    Spacer(modifier = Modifier.height(24.dp))

    HourlyForecast(hourlyForecast)

    Spacer(modifier = Modifier.height(24.dp))

    DailyForecast(dailyForecast)

    Spacer(modifier = Modifier.height(80.dp))
}
