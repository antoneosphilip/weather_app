package com.example.weather_app.data.weather.datasource.remote

import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{

    @GET("weather")
    suspend fun getWeather(
        @Query("appid") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric",
        @Query("cnt") count:Int=20

    ): WeatherForecastResponse

    @GET("forecast/daily")
    suspend fun getDailyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric",
        @Query("cnt") count:Int=7

    ): DailyForecastResponse

}