package com.example.weather_app.presentation.home.viewModel

import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    data class Success(
        val currentWeather: WeatherResponse,
        val hourlyForecast: WeatherForecastResponse,
        val dailyForecast: DailyForecastResponse
    ) : HomeUiState()
}