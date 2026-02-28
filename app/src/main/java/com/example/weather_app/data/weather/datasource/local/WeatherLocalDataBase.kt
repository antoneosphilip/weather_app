package com.example.weather_app.data.weather.datasource.local

import android.content.Context
import com.example.weather_app.data.weather.model.DailyEntity
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.HourlyEntity
import com.example.weather_app.data.weather.model.SettingModel
import com.example.weather_app.data.weather.model.WeatherEntity
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse
import com.example.weather_app.db.DataBase
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataBase (context: Context){

    private val weatherDao: WeatherDao= DataBase.getInstance(context).getWeatherDao()
    suspend fun insertWeather(weather: WeatherResponse) =
        weatherDao.insertWeather(WeatherEntity(response = weather))

    suspend fun insertHourlyForecast(hourly: WeatherForecastResponse) =
        weatherDao.insertHourlyForecast(HourlyEntity(response = hourly))

    suspend fun insertDailyForecast(daily: DailyForecastResponse) =
        weatherDao.insertDailyForecast(DailyEntity(response = daily))

    suspend fun getWeather(): WeatherResponse? =
        weatherDao.getWeather()?.response

    suspend fun getHourlyForecast(): WeatherForecastResponse? =
        weatherDao.getHourlyForecast()?.response

    suspend fun getDailyForecast(): DailyForecastResponse? =
        weatherDao.getDailyForecast()?.response




}