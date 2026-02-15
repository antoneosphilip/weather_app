package com.example.weather_app.data.weather.datasource

import com.example.weather_app.data.weather.model.WeatherResponse
import com.example.weather_app.network.RetrofitHelper
import retrofit2.http.Query

class WeatherRemoteDataSource{
    private val weatherService:WeatherService=RetrofitHelper.retrofitService
    suspend fun getWeather(
         lat: Double,
        lon: Double,
         apiKey: String,
        lang: String = "en",
        units: String = "metric"
    ):WeatherResponse{
        return weatherService.getWeather(
            lat=lat,
            lon=lon,
            apiKey=apiKey,
            lang=lang,
            units = units
        )

    }
}