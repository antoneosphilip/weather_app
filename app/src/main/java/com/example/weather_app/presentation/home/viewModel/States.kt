package com.example.weather_app.presentation.home.viewModel

import com.example.weather_app.data.weather.model.WeatherResponse

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: WeatherResponse) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}