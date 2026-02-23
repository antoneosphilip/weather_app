package com.example.weather_app.presentation.alert.viewModel

import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.data.favorite.model.LocationModel

sealed class AlertUiState {
    data object Loading : AlertUiState()


    data class Error(val message: String) : AlertUiState()
    data class Success(
        val alertList: List<AlertModel>,

        ) : AlertUiState()
}