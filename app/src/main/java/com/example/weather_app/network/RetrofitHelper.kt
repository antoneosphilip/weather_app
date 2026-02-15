package com.example.weather_app.network

import com.example.weather_app.data.weather.datasource.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofit= Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .build()

    val retrofitService: WeatherService = retrofit.create(WeatherService::class.java)
}