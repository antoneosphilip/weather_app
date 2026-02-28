package com.example.weather_app.presentation.favorite_details.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.constant.Constants
import com.example.weather_app.data.WeatherRepo
import kotlinx.coroutines.launch

class FavoriteDetailsViewModel(val context: Context,val lat:Double,val long:Double,private val weatherRepo: WeatherRepo ) : ViewModel() {



    var favoriteStates = mutableStateOf<FavoriteDetailsUiState>(FavoriteDetailsUiState.Loading)
        private set

    var windSpeedUnit = mutableStateOf("m/s")
        private set

    val temp = mutableStateOf("")

    init {
        viewModelScope.launch {
            weatherRepo.getSetting()?.let { it1 ->
                getAllWeatherData(lat,long,
                    it1.languageCode,it1.temperatureUnit)
                temp.value=getUnit(it1.temperatureUnit)
            }

            weatherRepo.observeSettings().collect { settings ->
                if (settings != null) {
                    getAllWeatherData(lat,long, settings.languageCode, settings.temperatureUnit)
                    temp.value = getUnit(settings.temperatureUnit)
                    windSpeedUnit.value = getWindSpeedUnit(settings.windSpeedUnit)

                }
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

    fun getAllWeatherData(lat: Double, lon: Double, lan: String = "en", unit: String = "metric") {
        favoriteStates.value = FavoriteDetailsUiState.Loading

        Log.i("Get Data", "getAllWeatherData: ")
        viewModelScope.launch {
            try {
                val weather = weatherRepo.getWeather(lat, lon, Constants.apiKey, lan, unit)
                val hourlyForecast = weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey, lan, unit)
                val dailyForecast = weatherRepo.getDailyForecast(lat, lon, Constants.apiKey, lan, unit)

                favoriteStates.value = FavoriteDetailsUiState.Success(weather, hourlyForecast, dailyForecast)
            }catch (e:Exception){
                favoriteStates.value = FavoriteDetailsUiState.Error(e.message ?: "Unknown error")

            }

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
        private val lat:Double,
        private val long:Double,
        private val weatherRepo: WeatherRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteDetailsViewModel(context,lat,long,weatherRepo) as T
        }
    }