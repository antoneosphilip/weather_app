package com.example.weather_app.data.weather.repo

import android.content.Context
import com.example.weather_app.data.weather.datasource.WeatherRemoteDataSource
import com.example.weather_app.data.weather.model.WeatherResponse

class WeatherRepo(context: Context){
    private val weatherRemoteData:WeatherRemoteDataSource= WeatherRemoteDataSource()

    suspend fun getWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        lang: String = "en",
        units: String = "metric"
    ):WeatherResponse{
     return  weatherRemoteData.getWeather(
            lat=lat,
            lon=lon,
            apiKey=apiKey,
            lang=lang,
            units = units
        )
    }
}