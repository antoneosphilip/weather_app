package com.example.weather_app.data.weather.datasource

import com.example.weather_app.data.weather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{

    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric"
    ): WeatherResponse

}