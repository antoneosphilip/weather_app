package com.example.weather_app.data.weather.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyForecastResponse(
    val city: DailyCity,
    val cod: String,
    val message: Double,
    val cnt: Int,
    val list: List<DailyForecastItem>
)

@Serializable
data class DailyCity(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int
)

@Serializable
data class DailyForecastItem(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: DailyTemp,
    @SerialName("feels_like")
    val feelsLike: DailyFeelsLike,
    val pressure: Int,
    val humidity: Int,
    val weather: List<Weather>,
    val speed: Double,
    val deg: Int,
    val gust: Double,
    val clouds: Int,
    val pop: Double
)

@Serializable
data class DailyTemp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@Serializable
data class DailyFeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)