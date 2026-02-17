package com.example.weather_app.data.weather.datasource

import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.WeatherForecastResponse
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
    suspend fun getHourlyForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        lang: String = "en",
        units: String = "metric",
        count:Int=20
    ): WeatherForecastResponse {
        return weatherService.getHourlyForecast(
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
        return weatherService.getDailyForecast(
            lat=lat,
            lon=lon,
            apiKey=apiKey,
            lang=lang,
            units = units,
            count = count
        )
    }
}