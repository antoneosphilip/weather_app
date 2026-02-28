package com.example.weather_app.network

import com.example.weather_app.data.location.datasource.GeoService
import com.example.weather_app.data.weather.datasource.remote.WeatherService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitHelper {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val geoRetrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/geo/1.0/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val retrofitService: WeatherService = retrofit.create(WeatherService::class.java)
    val geoService: GeoService = geoRetrofit.create(GeoService::class.java)
}