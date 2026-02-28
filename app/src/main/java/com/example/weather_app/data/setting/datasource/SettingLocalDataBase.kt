package com.example.weather_app.data.setting.datasource

import android.content.Context
import com.example.weather_app.data.weather.datasource.local.WeatherDao
import com.example.weather_app.data.weather.model.SettingModel
import com.example.weather_app.db.DataBase
import kotlinx.coroutines.flow.Flow

class SettingLocalDataBase (context: Context){

    private val settingDao: SettingDao = DataBase.getInstance(context).getSettingDao()


    suspend fun insertSetting(setting: SettingModel) {
        settingDao.insertSetting(setting)
    }

    suspend fun getSetting(): SettingModel? {
        return settingDao.getAllSetting()
    }
    fun observeSettings(): Flow<SettingModel?> = settingDao.observeSettings()



}