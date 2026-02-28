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
import com.example.weather_app.data.location.ILocationProvider
import com.example.weather_app.helper.NetworkObserver
import com.example.weather_app.prefs.PreferenceStorage
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class HomeViewModel(
    private val weatherRepo: WeatherRepo,
    private val prefs: PreferenceStorage,
    val locationProvider: ILocationProvider,
    private val networkMonitor: NetworkObserver
) : ViewModel() {

    val isConnected = networkMonitor.isConnected

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
                        lastLocationSetting = it.location
                        shouldNavigateToMap.value = true
                        shouldCloseMap.value = false
                    } else {
                        if (it.location == "GPS") {
                            shouldCloseMap.value = true
                            shouldNavigateToMap.value = false
                            getCurrentLocation()
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

        viewModelScope.launch {
            isConnected.collect { connected ->
                if (connected) {
                    val currentState = uiState.value
                    if (currentState is HomeUiState.Success && currentState.isOffline) {
                        latLong?.let { ll ->
                            val setting = weatherRepo.getSetting()
                            val language = setting?.languageCode ?: "en"
                            val unit = setting?.temperatureUnit ?: "metric"
                            getAllWeatherData(ll.latitude, ll.longitude, language, unit)
                            temp.value = getUnit(unit)
                        }
                    }
                }
            }
        }
    }

    fun retryLocation(context: Context) {
        viewModelScope.launch {
            if (latLong == null) {
                if (!locationProvider.hasPermission()) {
                    val lat = prefs.getString("lat").toDoubleOrNull()
                    val lon = prefs.getString("lon").toDoubleOrNull()
                    if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {
                        tryLoadFromCache()
                    } else {
                        openAppSettings(context)
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
                    prefs.save("lat", it.latitude)
                    prefs.save("lon", it.longitude)
                }
            }
        }
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    private fun tryLoadFromCache() {
        val lat = prefs.getString("lat").toDoubleOrNull()
        val lon = prefs.getString("lon").toDoubleOrNull()

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

    suspend fun getCurrentLocation() {
        latLong = locationProvider.getLocation()
        Log.i("lattttlongng", "getCurrentLocation: " + (latLong?.latitude ?: 0))
        if (latLong == null) {
            val lat = prefs.getString("lat")?.toDoubleOrNull()
            val lon = prefs.getString("lon")?.toDoubleOrNull()
            if (lat != null && lon != null) {
                latLong = LatLng(lat, lon)
            }
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
        viewModelScope.launch {
            val connected = isConnected.first()

            if (connected) {
                uiState.value = HomeUiState.Loading
                try {
                    supervisorScope {
                        val weatherDeferred = async { weatherRepo.getWeather(lat, lon, Constants.apiKey, lan, unit) }
                        val hourlyDeferred = async { weatherRepo.getHourlyForecast(lat, lon, Constants.apiKey, lan, unit) }
                        val dailyDeferred = async { weatherRepo.getDailyForecast(lat, lon, Constants.apiKey, lan, unit) }

                        val weather = weatherDeferred.await()
                        val hourly = hourlyDeferred.await()
                        val daily = dailyDeferred.await()

                        weatherRepo.insertWeather(weather)
                        weatherRepo.insertHourlyForecast(hourly)
                        weatherRepo.insertDailyForecast(daily)

                        uiState.value = HomeUiState.Success(weather, hourly, daily, isOffline = false)
                        latLong = LatLng(weather.coord.lat, weather.coord.lon)
                    }
                } catch (e: Exception) {
                    uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
                }
            } else {
                val weather = weatherRepo.getCachedWeather()
                val hourly = weatherRepo.getCachedHourlyForecast()
                val daily = weatherRepo.getCachedDailyForecast()

                if (weather != null && hourly != null && daily != null) {
                    uiState.value = HomeUiState.Success(weather, hourly, daily, isOffline = true)
                    latLong = LatLng(weather.coord.lat, weather.coord.lon)
                } else {
                    uiState.value = HomeUiState.Error("No internet connection")
                }
            }
        }
    }
}

class HomeViewModelFactory(
    private val weatherRepo: WeatherRepo,
    private val prefs: PreferenceStorage,
    private val locationProvider: LocationProvider,
    private val networkMonitor: NetworkObserver
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(weatherRepo, prefs, locationProvider, networkMonitor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}