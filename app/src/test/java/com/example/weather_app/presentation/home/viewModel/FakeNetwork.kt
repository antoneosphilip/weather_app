package com.example.weather_app.presentation.home.viewModel

import com.example.weather_app.helper.NetworkObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeNetworkMonitor(
    initialState: Boolean = true
) : NetworkObserver {

    private val _connectionState = MutableStateFlow(initialState)

    override val isConnected: Flow<Boolean>
        get() = _connectionState.asStateFlow()

    fun setConnected(isConnected: Boolean) {
        _connectionState.value = isConnected
    }
}