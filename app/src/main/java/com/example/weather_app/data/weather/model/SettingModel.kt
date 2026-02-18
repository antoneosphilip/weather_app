package com.example.weather_app.data.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable



@Serializable
@Entity(tableName = "setting")
data class SettingModel(
    @PrimaryKey
    val id: Int = 0,
    val location: String = "GPS",
    val languageCode: String = "en",
    val temperatureUnit: String = "metric",
    val windSpeedUnit: String = "meter"
)