package com.example.weather_app.presentation.setting.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.weather.model.SettingModel
import com.example.weather_app.data.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SettingViewModel(context: Context, private val weatherRepo: WeatherRepo) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow("en")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _selectedLocation = MutableStateFlow("GPS")
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _selectedTemperature = MutableStateFlow("metric")
    val selectedTemperature = _selectedTemperature.asStateFlow()

    private val _selectedWindSpeed = MutableStateFlow("meter")
    val selectedWindSpeed = _selectedWindSpeed.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = weatherRepo.getSetting()
            if (settings != null) {
                _selectedLanguage.value = settings.languageCode
                _selectedTemperature.value = settings.temperatureUnit
                _selectedLocation.value = settings.location
                _selectedWindSpeed.value = settings.windSpeedUnit
            }
        }
    }

    fun changeLanguage(code: String) {
        Log.d("SettingViewModel", "Changing language to $code")
        _selectedLanguage.value = code
        saveSettings()
    }

    fun changeLocation(code: String) {
        _selectedLocation.value = code
        saveSettings()
    }

    fun changeTemperature(code: String) {
        _selectedTemperature.value = code
        saveSettings()
    }

    fun changeWindSpeed(code: String) {
        _selectedWindSpeed.value = code
        saveSettings()
    }

    private fun saveSettings() {
        viewModelScope.launch {
            weatherRepo.insertSetting(
                SettingModel(
                    location = _selectedLocation.value,
                    languageCode = _selectedLanguage.value,
                    temperatureUnit = _selectedTemperature.value,
                    windSpeedUnit = _selectedWindSpeed.value
                )
            )
        }
    }
}

class SettingViewModelFactory(
    private val context: Context,
    private val weatherRepo: WeatherRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(context, weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}