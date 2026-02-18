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
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@Suppress("UNREACHABLE_CODE")
class HomeViewModel(
    private val context: Context,
) : ViewModel() {

    private val weatherRepo: WeatherRepo = WeatherRepo(context)


    val locationProvider: LocationProvider = LocationProvider(context)

    var uiState = mutableStateOf<HomeUiState>(HomeUiState.Loading)
        private set

    var shouldNavigateToMap = mutableStateOf(false)
        private set

    var latLong: LatLng? = null
        private set

    var isNavBefore = mutableStateOf(false)
        private set

    val temp=mutableStateOf("")
    init {
        viewModelScope.launch {
            loadWeatherData()
            weatherRepo.observeSettings().collect{
                it->
                if (it != null) {
                    if(it.location=="Manual"){
                        shouldNavigateToMap.value=true
                    }
                    latLong?.let {
                            it1 -> getAllWeatherData(it1.latitude,it1.longitude,it.languageCode,it.temperatureUnit)
                        temp.value=it.temperatureUnit
                    }
                }

            }
        }

    }


    fun onMapNavigationHandled() {
        shouldNavigateToMap.value = false
    }

    fun loadWeatherData() {
        uiState.value = HomeUiState.Loading

        viewModelScope.launch {
            try {

                latLong = locationProvider.getDeviceLocation()

                latLong?.let {
                    getAllWeatherData(
                        it.latitude,
                        it.longitude,
                        lan = SharedPreferencesHelper.getInstance(context).getString("language"),
                        unit = SharedPreferencesHelper.getInstance(context).getString("temperature")
                    )
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}", e)
                uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

     fun getAllWeatherData(lat: Double, lon: Double, lan: String = "en", unit: String = "metric") {
        Log.i("Get Data", "getAllWeatherData: ")
        viewModelScope.launch {
            val weather = weatherRepo.getWeather(lat, lon, Constants.apiKey, lan, unit)
            val hourlyForecast = weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey, lan, unit)
            val dailyForecast = weatherRepo.getDailyForecast(lat, lon, Constants.apiKey, lan, unit)

            uiState.value = HomeUiState.Success(weather, hourlyForecast, dailyForecast)
        }
    }

}


class HomeViewModelFactory(
    private val context: Context,
    private val viewModel: SettingViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}