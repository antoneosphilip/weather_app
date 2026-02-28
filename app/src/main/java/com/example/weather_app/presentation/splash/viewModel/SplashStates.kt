package com.example.weather_app.presentation.splash.viewModel

sealed class SplashEvent {
    object RequestPermission : SplashEvent()
    object NavigateToHome : SplashEvent()
    object ShowSettingsDialog : SplashEvent()
    object ShowToastAndRetry : SplashEvent()
}
