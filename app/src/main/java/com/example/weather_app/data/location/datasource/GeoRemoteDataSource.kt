package com.example.weather_app.data.location.datasource

import com.example.weather_app.constant.Constants
import com.example.weather_app.data.location.model.GeoLocation
import com.example.weather_app.data.weather.datasource.remote.WeatherService
import com.example.weather_app.network.RetrofitHelper

class LocationRemoteDataSource {
    private val weatherService: WeatherService = RetrofitHelper.retrofitService
    private val geoService: GeoService = RetrofitHelper.geoService


    suspend fun searchLocations(query: String): List<GeoLocation> {
        return geoService.searchLocations(query, 5, Constants.apiKey)
    }
}