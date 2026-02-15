package com.example.weather_app.presentation.home.viewModel

import LocationProvider
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.constant.Constants
import com.example.weather_app.data.weather.model.WeatherResponse
import com.example.weather_app.data.weather.repo.WeatherRepo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlin.math.log

class HomeViewModel(context:Context): ViewModel(){
    private val weatherRepo:WeatherRepo=WeatherRepo(context)
    val weatherResponse: MutableState<WeatherResponse?> = mutableStateOf(null)
    val error: MutableState<String> = mutableStateOf("")
    val isLoading:MutableState<Boolean> =mutableStateOf(false)
    val locationProvider:LocationProvider=LocationProvider(context)
    var latLong: LatLng = LatLng(0.0, 0.0)

    init {
        viewModelScope.launch {
            val location = locationProvider.getDeviceLocation()
            if (location != null) {
                latLong = location
                Log.i("Location", ": "+latLong.latitude+"   "+latLong.longitude)
                getWeather()
            } else {
            }
        }
    }
    fun getWeather(){
        isLoading.value=true
        viewModelScope.launch {
            try {
              val data=  weatherRepo.getWeather(latLong.latitude,latLong.longitude,Constants.apiKey,)
                weatherResponse.value=data
                Log.i("Data", "getWeather: "+data)
            }catch (e:Exception){
                error.value=e.message?:""
            }
            isLoading.value=false
        }
    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
