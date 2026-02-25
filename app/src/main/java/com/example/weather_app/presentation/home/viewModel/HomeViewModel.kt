package com.example.weather_app.presentation.home.viewModel

import LocationProvider
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.constant.Constants
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.prefs.SharedPreferencesHelper
import com.example.weather_app.presentation.favorite_details.viewModel.FavoriteDetailsUiState
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("UNREACHABLE_CODE")
class HomeViewModel(
    private val context: Context,private val weatherRepo: WeatherRepo
) : ViewModel() {

    val locationProvider: LocationProvider = LocationProvider(context)

    var uiState = mutableStateOf<HomeUiState>(HomeUiState.Loading)
        private set

    var shouldNavigateToMap = mutableStateOf(false)
        private set

    var latLong: LatLng? = null
        private set

    val shouldCloseMap = mutableStateOf(false)


    val temp=mutableStateOf("")
    init {
        viewModelScope.launch {
            latLong = locationProvider.getDeviceLocation()
            latLong?.let {
                val setting= weatherRepo.getSetting()
                val language = setting?.languageCode ?: "en"
                val unit = setting?.temperatureUnit ?: "metric"
                getAllWeatherData(it.latitude,it.longitude,
                    language,unit)
                temp.value=getUnit(unit)
                SharedPreferencesHelper.getInstance(context).save("lat",it.latitude)
                SharedPreferencesHelper.getInstance(context).save("lon",it.longitude)
            }
            launch {
                weatherRepo.observeSettings().collectLatest { it ->
                    if (it != null) {
                        if (it.location == "Manual") {
                            shouldNavigateToMap.value = true
                        } else {
                            shouldCloseMap.value = true
                            shouldNavigateToMap.value = false

                        }
                        latLong?.let { it1 ->
                            getAllWeatherData(
                                it1.latitude,
                                it1.longitude,
                                it.languageCode,
                                it.temperatureUnit
                            )
                            temp.value = getUnit(it.temperatureUnit)
                        }
                    }

                }
            }
        }

    }

    fun onMapClosed() {
        shouldCloseMap.value = false
    }
    fun onNavigatedToMap() {
        shouldNavigateToMap.value = false
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
         uiState.value = HomeUiState.Loading

         Log.i("Get Data", "getAllWeatherData: ")
         viewModelScope.launch {
             try {

                 val weatherDeferred = async {
                     weatherRepo.getWeather(lat, lon, Constants.apiKey, lan, unit)
                 }

                 val hourlyDeferred = async {
                     weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey, lan, unit)
                 }

                 val dailyDeferred = async {
                     weatherRepo.getDailyForecast(lat, lon, Constants.apiKey, lan, unit)
                 }

                 val weather = weatherDeferred.await()
                 val hourlyForecast = hourlyDeferred.await()
                 val dailyForecast = dailyDeferred.await()

                 uiState.value =
                     HomeUiState.Success(weather, hourlyForecast, dailyForecast)

             } catch (e: Exception) {
                 uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
             }
         }
    }

}


class HomeViewModelFactory(
    private val context: Context,
    private val weatherRepo: WeatherRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(context,weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}