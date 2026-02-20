package com.example.weather_app.presentation.alert.viewModel

data class AlertUiState(
    val showDialog: Boolean = false,
    val startTime: String = "",
    val endTime: String = "",
    val selectedType: String = "Alarm"
)