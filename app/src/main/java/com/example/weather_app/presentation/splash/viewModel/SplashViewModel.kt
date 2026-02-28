package com.example.weather_app.presentation.splash.viewModel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashViewModel : ViewModel() {

    private val _event = MutableStateFlow<SplashEvent?>(null)
    val event: StateFlow<SplashEvent?> = _event

    private var animationFinished = false
    private var retryCount = 0

    fun onAnimationFinished(hasPermission: Boolean) {
        animationFinished = true
        if (hasPermission) {
            _event.value = SplashEvent.NavigateToHome
        } else {
            _event.value = SplashEvent.RequestPermission
        }
    }

    fun onPermissionResult(granted: Boolean, canAskAgain: Boolean) {
        if (granted) {
            _event.value = SplashEvent.NavigateToHome
        } else {
            if (canAskAgain) {
                retryCount++
                _event.value = SplashEvent.ShowToastAndRetry
            } else {
                _event.value = SplashEvent.ShowSettingsDialog
            }
        }
    }

    fun onRetryFromDialog() {
        _event.value = SplashEvent.RequestPermission
    }

    fun onReturnFromSettings(hasPermission: Boolean) {
        if (hasPermission) {
            _event.value = SplashEvent.NavigateToHome
        } else {
            _event.value = SplashEvent.ShowSettingsDialog
        }
    }

    fun clearEvent() {
        _event.value = null
    }
}