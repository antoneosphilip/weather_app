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
import com.example.weather_app.data.weather.repo.WeatherRepo
import com.example.weather_app.prefs.SharedPreferencesHelper
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("UNREACHABLE_CODE")
class HomeViewModel(
    private val context: Context,
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

    var shouldNavigateToMap = mutableStateOf(false)
        private set

    var latLong: LatLng? = null
        private set

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
        viewModelScope.launch {
            settingViewModel.selectedLocation.collect { locationMode ->
                if (locationMode == "Manual") {
                    shouldNavigateToMap.value = true

                }
            }
        }
    }

    private fun refreshWeatherData() {
        latLong?.let { location ->
            viewModelScope.launch {
                try {
                    Log.d("HomeViewModel", "Refreshing weather data...")
                    getAllWeatherData(
                        location.latitude,
                        location.longitude,
                        lan = settingViewModel.getLanguageCode(),
                        unit = settingViewModel.getTemperatureUnit()
                    )
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error: ${e.message}", e)
                    uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

    fun getTemperatureUnit(): String {
        return when (settingViewModel.getTemperatureUnit()) {
            "metric" -> "C"
            "standard" -> "K"
            else -> "F"
        }
    }

    fun getCurrentLocation(): LatLng? = latLong

    fun updateLocationFromMap(latitude: Double, longitude: Double) {
        latLong = LatLng(latitude, longitude)
        shouldNavigateToMap.value = false
        viewModelScope.launch {
            try {
                uiState.value = HomeUiState.Loading
                getAllWeatherData(
                    latitude,
                    longitude,
                    lan = settingViewModel.getLanguageCode(),
                    unit = settingViewModel.getTemperatureUnit()
                )
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}", e)
                uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onMapNavigationHandled() {
        shouldNavigateToMap.value = false
    }

    fun loadWeatherData() {
        if (settingViewModel.selectedLocation.value == "Manual" && latLong == null) {
            shouldNavigateToMap.value = true
            return
        }

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

                getAllWeatherData(
                    location.latitude,
                    location.longitude,
                    lan = SharedPreferencesHelper.getInstance(context).getString("language"),
                    unit = SharedPreferencesHelper.getInstance(context).getString("temperature")
                )

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}", e)
                uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun getAllWeatherData(lat: Double, lon: Double, lan: String = "en", unit: String = "metric") {
        Log.i("Get Data", "getAllWeatherData: ")
        viewModelScope.launch {
            val weather = weatherRepo.getWeather(lat, lon, Constants.apiKey, lan, unit)
            val hourlyForecast = weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey, lan, unit)
            val dailyForecast = weatherRepo.getDailyForecast(lat, lon, Constants.apiKey, lan, unit)

            uiState.value = HomeUiState.Success(weather, hourlyForecast, dailyForecast)
        }
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

class HomeViewModelFactory(
    private val context: Context,
    private val viewModel: SettingViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(context, viewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}