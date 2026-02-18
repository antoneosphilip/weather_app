package com.example.weather_app.data.favorite.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "location")
data class LocationModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val lat: Double,
    val long:Double,
    val location: String,
    )