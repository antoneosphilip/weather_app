package com.example.weather_app.data.weather.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.data.weather.model.DailyEntity
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.HourlyEntity
import com.example.weather_app.data.weather.model.SettingModel
import com.example.weather_app.data.weather.model.WeatherEntity
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourly:HourlyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(daily:DailyEntity)

    @Query("SELECT * FROM weather")
    suspend fun getWeather(): WeatherEntity?

    @Query("SELECT * FROM hourly")
    suspend fun getHourlyForecast(): HourlyEntity

    @Query("SELECT * FROM daily")
    suspend fun getDailyForecast(): DailyEntity

}


