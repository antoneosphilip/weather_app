package com.example.weather_app.presentation.home.viewModel

import LocationProvider
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.constant.Constants
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.Weather
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse
import com.example.weather_app.data.weather.repo.WeatherRepo
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.math.log

@Suppress("UNREACHABLE_CODE")
class HomeViewModel(private val context: Context,
                    private val settingViewModel: SettingViewModel
    ) : ViewModel() {

    private val weatherRepo: WeatherRepo = WeatherRepo(context)
    val locationProvider: LocationProvider = LocationProvider(context)

    var uiState = mutableStateOf<HomeUiState>(HomeUiState.Loading)
        private set

    var needsPermissionRequest = mutableStateOf(false)
        private set

    var needsLocationSettings = mutableStateOf<IntentSenderRequest?>(null)
        private set

    private var latLong: LatLng? = null

    init {
        loadWeatherData()
        viewModelScope.launch {
            settingViewModel.selectedLanguage.collect { _ ->
                refreshWeatherData()
            }
        }

        viewModelScope.launch {
            settingViewModel.selectedTemperature.collect { _ ->
                refreshWeatherData()
            }
        }
    }
    private fun refreshWeatherData() {
        latLong?.let { location ->
            viewModelScope.launch {
                try {
                    Log.d("HomeViewModel", "Refreshing weather data...")
                    val (weather, hourly, daily) = getAllWeatherData(
                        location.latitude,
                        location.longitude,
                        lan = settingViewModel.getLanguageCode(),
                        unit = settingViewModel.getTemperatureUnit()
                    )
                    uiState.value = HomeUiState.Success(weather, hourly, daily)
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error: ${e.message}", e)
                    uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

     fun getTemperatureUnit():String{
        if(settingViewModel.getTemperatureUnit()=="metric"){
            return "C"
        }
        else if(settingViewModel.getTemperatureUnit()=="standard"){
            return "K"
        }
        else{
            return  "F"
        }
    }


    fun loadWeatherData() {
        uiState.value = HomeUiState.Loading
        needsPermissionRequest.value = false
        needsLocationSettings.value = null

        viewModelScope.launch {
            try {
                if (!locationProvider.hasLocationPermission()) {
                    uiState.value = HomeUiState.Loading
                    needsPermissionRequest.value = true
                    return@launch
                }

                if (!locationProvider.isLocationEnabled()) {
                    requestLocationSettingsInternal()
                    return@launch
                }

                var location = locationProvider.getDeviceLocation()

                if (location == null) {
                    delay(2000)
                    location = locationProvider.getDeviceLocation()
                }

                if (location == null) {
                    delay(3000)
                    location = locationProvider.getDeviceLocation()
                }

                if (location == null) {
                    uiState.value = HomeUiState.Error("Unable to get your location. Please try again.")
                    return@launch
                }

                latLong = location
                Log.i("Location", "Lat: ${location.latitude}, Lon: ${location.longitude}")

                val (weather, hourly, daily) = getAllWeatherData(
                    location.latitude,
                    location.longitude,

                    )

                uiState.value = HomeUiState.Success(weather, hourly, daily)

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}", e)
                uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    suspend fun getAllWeatherData(lat: Double, lon: Double,lan:String="en",unit:String="metric"):
            Triple<WeatherResponse, WeatherForecastResponse, DailyForecastResponse> {
        Log.i("Get Data", "getAllWeatherData: ")
        val weather = weatherRepo.getWeather(lat, lon, Constants.apiKey,lan,unit)
        val hourlyForecast = weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey,lan,unit)
        val dailyForecast = weatherRepo.getDailyForecast(lat, lon, Constants.apiKey,lan,unit)

        return Triple(weather, hourlyForecast, dailyForecast)
    }


    fun onPermissionResult(granted: Boolean) {
        needsPermissionRequest.value = false
        if (granted) {
            loadWeatherData()
        } else {
            viewModelScope.launch {
                delay(2000)
                loadWeatherData()
            }
        }
    }

    fun onLocationSettingsResult() {
        needsLocationSettings.value = null
        loadWeatherData()
    }

    private fun requestLocationSettingsInternal() {
        val activity = context as? Activity
        if (activity != null) {
            locationProvider.requestLocationSettings(
                activity = activity,
                onSuccess = { loadWeatherData() },
                onFailure = { intentSenderRequest ->
                    needsLocationSettings.value = intentSenderRequest
                }
            )
        }
    }
}

class HomeViewModelFactory(private val context: Context,private val viewModel: SettingViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(context,viewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}