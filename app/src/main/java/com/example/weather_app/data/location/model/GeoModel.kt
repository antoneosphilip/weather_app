package com.example.weather_app.data.location.model


import kotlinx.serialization.Serializable

@Serializable
data class GeoLocation(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)