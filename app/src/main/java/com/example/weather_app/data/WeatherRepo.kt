package com.example.weather_app.data

import android.content.Context
import com.example.weather_app.data.alert.datasouce.AlertLocalDataBase
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.data.favorite.datasource.FavoriteLocalDataBase
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.data.weather.datasource.local.WeatherLocalDataBase
import com.example.weather_app.data.weather.datasource.remote.WeatherRemoteDataSource
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.SettingModel
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherRepo(context: Context) {
    private val weatherRemoteData = WeatherRemoteDataSource()
    private val weatherLocalData = WeatherLocalDataBase(context)
    private val favoriteLocalDataBase=FavoriteLocalDataBase(context)
    private val alertLocalDataBase= AlertLocalDataBase(context)

    suspend fun getWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        lang: String = "en",
        units: String = "metric"
    ): WeatherResponse {
        return weatherRemoteData.getWeather(
            lat = lat,
            lon = lon,
            apiKey = apiKey,
            lang = lang,
            units = units
        )
    }

    suspend fun getHourlyForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        lang: String = "en",
        units: String = "metric",
        count: Int = 20
    ): WeatherForecastResponse {
        return weatherRemoteData.getHourlyForecast(
            lat = lat,
            lon = lon,
            apiKey = apiKey,
            lang = lang,
            units = units,
            count = count
        )
    }

    suspend fun getDailyForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        lang: String = "en",
        units: String = "metric",
        count: Int = 7
    ): DailyForecastResponse {
        return weatherRemoteData.getDailyForecast(
            lat = lat,
            lon = lon,
            apiKey = apiKey,
            lang = lang,
            units = units,
            count = count
        )
    }

    suspend fun insertSetting(setting: SettingModel) {
        weatherLocalData.insertSetting(setting)
    }

    suspend fun getSetting():SettingModel? {
      return  weatherLocalData.getSetting()
    }

     fun observeSettings(): Flow<SettingModel?> = weatherLocalData.observeSettings()

    suspend fun saveLocation(locationModel: LocationModel){
        favoriteLocalDataBase.saveLocation(locationModel)
    }

     fun getLocation() :Flow<List<LocationModel>>{
       return favoriteLocalDataBase.getLocation()
    }


   suspend fun saveAlert(alertModel: AlertModel){
        alertLocalDataBase.saveAlert(alertModel)
    }

    fun getAlert():Flow<List<AlertModel>>
    {
        return alertLocalDataBase.getAlert()
    }
    suspend fun deleteAlert(alertId: Int)
    {
         alertLocalDataBase.deleteAlert(alertId)
    }

}

