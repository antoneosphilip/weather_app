package com.example.weather_app.data.weather.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.data.weather.model.SettingModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: SettingModel)

    @Query("SELECT * FROM setting")
    suspend fun getAllSetting(): SettingModel?

    @Query("SELECT * FROM setting")
     fun observeSettings(): Flow<SettingModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(setting: SettingModel)

}


