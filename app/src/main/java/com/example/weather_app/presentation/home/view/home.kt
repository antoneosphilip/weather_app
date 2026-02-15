package com.example.weather_app.presentation.home.view

import WeatherIcon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.data.weather.model.DailyForecastItem
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.ForecastItem
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.presentation.components.CustomLoading
import com.example.weather_app.presentation.components.ErrorMessage
import com.example.weather_app.presentation.home.viewModel.HomeUiState
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.example.weather_app.presentation.home.viewModel.HomeViewModelFactory
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        when (val state = viewModel.uiState.value) {

            is HomeUiState.Loading -> {
                CustomLoading()
            }

            is HomeUiState.Error -> {
                ErrorMessage(
                    error = state.message,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is HomeUiState.Success -> {
                val weather = state.currentWeather
                val hourlyForecast = state.hourlyForecast
                val dailyForecast = state.dailyForecast

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
                        icon = weather.weather.firstOrNull()?.icon ?: "",
                        feelsLike = weather.main.feelsLike.roundToInt(),
                        timestamp = weather.dt,
                        condition = weather.weather.firstOrNull()?.description ?: "",
                    )

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
            }
        }
    }
}