package com.example.weather_app.data.setting.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.data.weather.model.SettingModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: SettingModel)

    @Query("SELECT * FROM setting")
    suspend fun getAllSetting(): SettingModel?

    @Query("SELECT * FROM setting")
    fun observeSettings(): Flow<SettingModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(setting: SettingModel)
}