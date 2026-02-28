package com.example.weather_app.presentation.location.viewModel

import com.example.weather_app.data.location.model.GeoLocation

sealed class LocationPickerState {
    object Idle : LocationPickerState()
    object Loading : LocationPickerState()
    data class SearchResults(val results: List<GeoLocation>) : LocationPickerState()
    data class LocationSelected(val lat: Double, val lon: Double, val address: String) : LocationPickerState()
    data class Error(val message: String) : LocationPickerState()
}
