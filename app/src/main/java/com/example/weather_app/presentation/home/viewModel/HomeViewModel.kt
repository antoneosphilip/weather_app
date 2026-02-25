package com.example.weather_app.presentation.home.viewModel

import LocationProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.constant.Constants
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.prefs.SharedPreferencesHelper
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val context: Context,
    private val weatherRepo: WeatherRepo
) : ViewModel() {

    val locationProvider = LocationProvider(context)

    var uiState = mutableStateOf<HomeUiState>(HomeUiState.Loading)
        private set

    var shouldNavigateToMap = mutableStateOf(false)
        private set

    var latLong: LatLng? = null
        private set

    val shouldCloseMap = mutableStateOf(false)
    val temp = mutableStateOf("")

    var deniedCount = 0

    private var lastLocationSetting: String? = null

    init {
        viewModelScope.launch {
            weatherRepo.observeSettings().collect {
                if (it != null) {
                    if (it.location == "Manual" && lastLocationSetting != "Manual") {
                        shouldNavigateToMap.value = true
                    } else if (it.location != "Manual") {
                        shouldCloseMap.value = true
                        shouldNavigateToMap.value = false
                    }
                    lastLocationSetting = it.location
                    latLong?.let { ll ->
                        getAllWeatherData(ll.latitude, ll.longitude, it.languageCode, it.temperatureUnit)
                        temp.value = getUnit(it.temperatureUnit)
                    }
                }
            }
        }
    }

    fun retryLocation() {
        viewModelScope.launch {
            if(latLong==null){
            if (!locationProvider.hasPermission()) {
                val lat = SharedPreferencesHelper.getInstance(context).getString("lat").toDoubleOrNull()
                val lon = SharedPreferencesHelper.getInstance(context).getString("lon").toDoubleOrNull()
                if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {
                    tryLoadFromCache()
                } else {
                    openAppSettings()
                }
                return@launch
            }

            latLong = locationProvider.getLocation()
            if (latLong == null) {
                tryLoadFromCache()
                return@launch
            }
            deniedCount = 0
            latLong?.let {
                val setting = weatherRepo.getSetting()
                val language = setting?.languageCode ?: "en"
                val unit = setting?.temperatureUnit ?: "metric"
                getAllWeatherData(it.latitude, it.longitude, language, unit)
                temp.value = getUnit(unit)
                SharedPreferencesHelper.getInstance(context).save("lat", it.latitude)
                SharedPreferencesHelper.getInstance(context).save("lon", it.longitude)
            }
                }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
    private fun tryLoadFromCache() {
        val lat = SharedPreferencesHelper.getInstance(context).getString("lat").toDoubleOrNull()
        val lon = SharedPreferencesHelper.getInstance(context).getString("lon").toDoubleOrNull()

        if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {
            viewModelScope.launch {
                val setting = weatherRepo.getSetting()
                val language = setting?.languageCode ?: "en"
                val unit = setting?.temperatureUnit ?: "metric"
                getAllWeatherData(lat, lon, language, unit)
                temp.value = getUnit(unit)
            }
        } else {
            uiState.value = HomeUiState.Error("Please enable location to continue")
        }
    }

    private val _showLocationDialog = MutableSharedFlow<Unit>(replay = 1)
    val showLocationDialog = _showLocationDialog.asSharedFlow()

    fun onUserDeniedLocation() {
        deniedCount++
        if (deniedCount < 2) {
            viewModelScope.launch {
                _showLocationDialog.emit(Unit)
            }
        } else {
            showLocationError()
        }
    }

    fun requestLocationCheck() {
        if (uiState.value is HomeUiState.Success) return
        if (latLong != null) return
        viewModelScope.launch {
            _showLocationDialog.emit(Unit)
        }
    }

    fun showLocationError() {
        tryLoadFromCache()
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
        viewModelScope.launch {
            try {
                val weatherDeferred = async { weatherRepo.getWeather(lat, lon, Constants.apiKey, lan, unit) }
                val hourlyDeferred = async { weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey, lan, unit) }
                val dailyDeferred = async { weatherRepo.getDailyForecast(lat, lon, Constants.apiKey, lan, unit) }

                uiState.value = HomeUiState.Success(
                    weatherDeferred.await(),
                    hourlyDeferred.await(),
                    dailyDeferred.await()
                )
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
            return HomeViewModel(context, weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}