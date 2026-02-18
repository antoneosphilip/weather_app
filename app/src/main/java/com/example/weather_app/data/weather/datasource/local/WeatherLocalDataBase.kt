package com.example.weather_app.data.weather.datasource.local

import android.content.Context
import com.example.weather_app.data.weather.model.SettingModel
import com.example.weather_app.db.DataBase
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataBase (context: Context){

    private val weatherDao: WeatherDao= DataBase.getInstance(context).getWeatherDao()


    suspend fun insertSetting(setting: SettingModel) {
        weatherDao.insertSetting(setting)
    }

    suspend fun getSetting(): SettingModel? {
        return weatherDao.getAllSetting()
    }
     fun observeSettings(): Flow<SettingModel?> = weatherDao.observeSettings()



}