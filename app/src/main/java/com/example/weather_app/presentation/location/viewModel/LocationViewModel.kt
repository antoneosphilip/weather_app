package com.example.weather_app.presentation.location.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.constant.Constants
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class LocationPickerViewModel(
    private val weatherRepo: WeatherRepo
) : ViewModel() {

    private val _savedState = MutableStateFlow<SavedState>(SavedState.Idle)
    val savedState = _savedState.asStateFlow()

    fun selectHomeLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            _savedState.value = SavedState.Loading
            try {
                val setting = weatherRepo.getSetting()
                val language = setting?.languageCode ?: "en"
                val unit = setting?.temperatureUnit ?: "metric"
                supervisorScope {
                    val weatherDeferred = async { weatherRepo.getWeather(lat, lon, Constants.apiKey, language, unit) }
                    val hourlyDeferred = async { weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey, language, unit) }
                    val dailyDeferred = async { weatherRepo.getDailyForecast(lat, lon, Constants.apiKey, language, unit) }
                    weatherDeferred.await()
                    hourlyDeferred.await()
                    dailyDeferred.await()
                }
                _savedState.value = SavedState.HomeSuccess(lat, lon)
            } catch (e: Exception) {
                _savedState.value = SavedState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun saveFavoriteLocation(lat: Double, lon: Double, address: String) {
        viewModelScope.launch {
            _savedState.value = SavedState.Loading
            try {
                weatherRepo.saveLocation(
                    LocationModel(lat = lat, long = lon, location = address)
                )
                _savedState.value = SavedState.FavoriteSuccess
            } catch (e: Exception) {
                _savedState.value = SavedState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class SavedState {
        object Idle : SavedState()
        object Loading : SavedState()
        object FavoriteSuccess : SavedState()
        data class HomeSuccess(val lat: Double, val lon: Double) : SavedState()
        data class Error(val message: String) : SavedState()
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