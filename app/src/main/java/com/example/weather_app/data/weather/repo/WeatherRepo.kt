package com.example.weather_app.data.weather.repo

import android.content.Context
import com.example.weather_app.data.weather.datasource.WeatherRemoteDataSource
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.WeatherForecastResponse
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
    suspend fun getHourlyForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        lang: String = "en",
        units: String = "metric",
        count:Int=20
    ): WeatherForecastResponse {
        return  weatherRemoteData.getHourlyForecast(
            lat=lat,
            lon=lon,
            apiKey=apiKey,
            lang=lang,
            units = units,
            count = count
        )
    }
    suspend fun getDailyForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        lang: String = "en",
        units: String = "metric",
        count:Int=7
    ): DailyForecastResponse {
        return  weatherRemoteData.getDailyForecast(
            lat=lat,
            lon=lon,
            apiKey=apiKey,
            lang=lang,
            units = units,
            count = count
        )
    }
}