package com.example.weather_app.helper

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    val isConnected: Flow<Boolean>
}