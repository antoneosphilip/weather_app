package com.example.weather_app.data.weather.model


import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WeatherTypeConverters {

    @TypeConverter
    fun fromWeatherResponse(value: WeatherResponse): String =
        Json.encodeToString(value)

    @TypeConverter
    fun toWeatherResponse(value: String): WeatherResponse =
        Json.decodeFromString(value)

    @TypeConverter
    fun fromHourlyResponse(value: WeatherForecastResponse): String =
        Json.encodeToString(value)

    @TypeConverter
    fun toHourlyResponse(value: String): WeatherForecastResponse =
        Json.decodeFromString(value)

    @TypeConverter
    fun fromDailyResponse(value: DailyForecastResponse): String =
        Json.encodeToString(value)

    @TypeConverter
    fun toDailyResponse(value: String): DailyForecastResponse =
        Json.decodeFromString(value)
}