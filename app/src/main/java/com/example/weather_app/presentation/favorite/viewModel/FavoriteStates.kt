package com.example.weather_app.presentation.favorite.viewModel

import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse

sealed class FavoriteUiState {
    data object Loading : FavoriteUiState()


    data class Error(val message: String) : FavoriteUiState()
    data class Success(
        val favoriteList: List<LocationModel>,

    ) : FavoriteUiState()

    data class SaveSuccess(
        val favoriteModel: LocationModel,

        ) : FavoriteUiState()
    data object DeleteSuccess : FavoriteUiState()
}

