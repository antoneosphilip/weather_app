package com.example.weather_app.data.alert.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

    @Serializable
    @Entity(tableName = "alerts")
    data class AlertModel(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val startTime: Long,
        val endTime: Long? = null,
        val type: String,
        val label: String? = null,
        var isActive: Boolean = true
    )