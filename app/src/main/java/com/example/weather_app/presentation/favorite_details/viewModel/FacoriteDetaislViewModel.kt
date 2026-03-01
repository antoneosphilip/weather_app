package com.example.weather_app.presentation.favorite_details.viewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.constant.Constants
import com.example.weather_app.data.WeatherRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class FavoriteDetailsViewModel(
    val context: Context,
    val lat: Double,
    val long: Double,
    private val weatherRepo: WeatherRepo
) : ViewModel() {

    var favoriteStates = mutableStateOf<FavoriteDetailsUiState>(FavoriteDetailsUiState.Loading)
        private set

    var windSpeedUnit = mutableStateOf("m/s")
        private set

    val temp = mutableStateOf("")

    init {
        viewModelScope.launch {
            val initial = weatherRepo.getSetting()
            val language = initial?.languageCode ?: "en"
            val unit = initial?.temperatureUnit ?: "metric"
            val windUnit = initial?.windSpeedUnit ?: "meter"

            temp.value = getUnit(unit)
            windSpeedUnit.value = getWindSpeedUnit(windUnit)
            getAllWeatherData(lat, long, language, unit)
        }

        viewModelScope.launch {
            weatherRepo.observeSettings().collect { settings ->
                if (settings != null) {
                    temp.value = getUnit(settings.temperatureUnit)
                    windSpeedUnit.value = getWindSpeedUnit(settings.windSpeedUnit)
                    getAllWeatherData(lat, long, settings.languageCode, settings.temperatureUnit)
                }
            }
        }
    }

    fun getAllWeatherData(lat: Double, lon: Double, lan: String = "en", unit: String = "metric") {
        viewModelScope.launch {
            favoriteStates.value = FavoriteDetailsUiState.Loading
            try {
                supervisorScope {
                    val weatherDeferred = async { weatherRepo.getWeather(lat, lon, Constants.apiKey, lan, unit) }
                    val hourlyDeferred = async { weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey, lan, unit) }
                    val dailyDeferred = async { weatherRepo.getDailyForecast(lat, lon, Constants.apiKey, lan, unit) }

                    val weather = weatherDeferred.await()
                    val hourly = hourlyDeferred.await()
                    val daily = dailyDeferred.await()

                    favoriteStates.value = FavoriteDetailsUiState.Success(weather, hourly, daily)
                }
            } catch (e: Exception) {
                favoriteStates.value = FavoriteDetailsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getUnit(unit: String): String {
        return when (unit.lowercase()) {
            "metric" -> "°C"
            "imperial" -> "°F"
            "standard" -> "K"
            else -> ""
        }
    }

    fun getWindSpeedUnit(unit: String): String {
        return when (unit) {
            "mile" -> "mph"
            else -> "m/s"
        }
    }

    fun convertWindSpeed(speedMs: Double, unit: String): Double {
        return if (unit == "mile") speedMs * 2.237 else speedMs
    }
}

class FavoriteDetailsViewModelFactory(
    private val context: Context,
    private val lat: Double,
    private val long: Double,
    private val weatherRepo: WeatherRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteDetailsViewModel(context, lat, long, weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}