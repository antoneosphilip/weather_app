package com.example.weather_app.data.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val id: Int = 1,
    val response: WeatherResponse
)

@Entity(tableName = "hourly")
data class HourlyEntity(
    @PrimaryKey
    val id: Int = 1,
    val response: WeatherForecastResponse
)

@Entity(tableName = "daily")
data class DailyEntity(
    @PrimaryKey
    val id: Int = 1,
    val response: DailyForecastResponse
)