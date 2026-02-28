package com.example.weather_app.presentation.location.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.WeatherRepo
import kotlinx.coroutines.launch


class LocationPickerViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {

    var state = mutableStateOf<LocationPickerState>(LocationPickerState.Idle)
        private set

    fun onLocationSelected(lat: Double, lon: Double, address: String) {
        state.value = LocationPickerState.LocationSelected(lat, lon, address)
    }

    fun searchLocations(query: String) {
        if (query.length < 2) {
            state.value = LocationPickerState.Idle
            return
        }
        viewModelScope.launch {
            state.value = LocationPickerState.Loading
            try {
                val results = weatherRepo.searchLocations(query)
                state.value = LocationPickerState.SearchResults(results)
            } catch (e: Exception) {
                state.value = LocationPickerState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clearSearch() {
        state.value = LocationPickerState.Idle
    }
}

class LocationPickerViewModelFactory(
    private val weatherRepo: WeatherRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationPickerViewModel(weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}