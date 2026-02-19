package com.example.weather_app.presentation.favorite_details.viewModel


import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse

sealed class FavoriteDetailsUiState {
    data object Loading : FavoriteDetailsUiState()


    data class Error(val message: String) : FavoriteDetailsUiState()
    data class Success(
        val currentWeather: WeatherResponse,
        val hourlyForecast: WeatherForecastResponse,
        val dailyForecast: DailyForecastResponse

        ) : FavoriteDetailsUiState()
}