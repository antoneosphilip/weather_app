package com.example.weather_app.presentation.setting.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_app.prefs.SharedPreferencesHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class SettingViewModel(context: Context) : ViewModel() {

    private val prefsHelper = SharedPreferencesHelper.getInstance(context)

    private val KEY_LANGUAGE = "language"
    private val KEY_LOCATION = "location"
    private val KEY_TEMPERATURE = "temperature"
    private val KEY_WIND_SPEED = "wind_speed"

    private val _selectedLanguage = MutableStateFlow(
        prefsHelper.getString(KEY_LANGUAGE, "English")
    )
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _selectedLocation = MutableStateFlow(
        prefsHelper.getString(KEY_LOCATION, "GPS")
    )
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _selectedTemperature = MutableStateFlow(
        prefsHelper.getString(KEY_TEMPERATURE, "Celsius (°C)")
    )
    val selectedTemperature = _selectedTemperature.asStateFlow()

    private val _selectedWindSpeed = MutableStateFlow(
        prefsHelper.getString(KEY_WIND_SPEED, "Meter/Sec")
    )
    val selectedWindSpeed = _selectedWindSpeed.asStateFlow()

    fun changeLanguage(value: String) {
        Log.d("SettingViewModel", "Changing language from ${_selectedLanguage.value} to $value")

        _selectedLanguage.value = value
        prefsHelper.save(KEY_LANGUAGE, value)
    }

    fun changeLocation(value: String) {
        _selectedLocation.value = value
        prefsHelper.save(KEY_LOCATION, value)
    }

    fun changeTemperature(value: String) {
        _selectedTemperature.value = value
        prefsHelper.save(KEY_TEMPERATURE, value)
    }

    fun changeWindSpeed(value: String) {
        _selectedWindSpeed.value = value
        prefsHelper.save(KEY_WIND_SPEED, value)
    }

    fun getTemperatureUnit(): String {
        return when (_selectedTemperature.value) {
            "Celsius (°C)" -> "metric"
            "Fahrenheit (°F)" -> "imperial"
            "Kelvin (K)" -> "standard"
            else -> "metric"
        }
    }

    fun getLanguageCode(): String {
        return when (_selectedLanguage.value) {
            "English" -> "en"
            "Arabic" -> "ar"
            else -> "en"
        }
    }
}

class SettingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}