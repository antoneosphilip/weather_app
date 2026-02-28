package com.example.weather_app.data

import android.content.Context
import com.example.weather_app.data.alert.datasouce.AlertLocalDataBase
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.data.favorite.datasource.FavoriteLocalDataBase
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.data.location.datasource.LocationRemoteDataSource
import com.example.weather_app.data.location.model.GeoLocation
import com.example.weather_app.data.setting.datasource.SettingLocalDataBase
import com.example.weather_app.data.weather.datasource.local.WeatherLocalDataBase
import com.example.weather_app.data.weather.datasource.remote.WeatherRemoteDataSource
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.SettingModel
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse
import com.example.weather_app.db.DataBase
import kotlinx.coroutines.flow.Flow

class WeatherRepo(
    private val weatherLocalData: WeatherLocalDataBase,
    private val favoriteLocalDataBase: FavoriteLocalDataBase,
    private val alertLocalDataBase: AlertLocalDataBase,
    private val weatherRemoteData: WeatherRemoteDataSource,
    private val settingLocalDataBase: SettingLocalDataBase,
    private val locationRemoteDataSource: LocationRemoteDataSource

) {

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
        settingLocalDataBase.insertSetting(setting)
    }

    suspend fun getSetting():SettingModel? {
      return  settingLocalDataBase.getSetting()
    }

     fun observeSettings(): Flow<SettingModel?> = settingLocalDataBase.observeSettings()

    suspend fun saveLocation(locationModel: LocationModel){
        favoriteLocalDataBase.saveLocation(locationModel)
    }

     fun getLocation() :Flow<List<LocationModel>>{
       return favoriteLocalDataBase.getLocation()
    }

    suspend  fun deleteLocation(locationId:Int) {
        return favoriteLocalDataBase.deleteLocation(locationId)
    }


   suspend fun saveAlert(alertModel: AlertModel):Long{
       return alertLocalDataBase.saveAlert(alertModel)
    }

    fun getAlert():Flow<List<AlertModel>>
    {
        return alertLocalDataBase.getAlert()
    }
    suspend fun deleteAlert(alertId: Int)
    {
         alertLocalDataBase.deleteAlert(alertId)
    }
    suspend fun insertWeather(weather: WeatherResponse) =
        weatherLocalData.insertWeather(weather)

    suspend fun insertHourlyForecast(hourly: WeatherForecastResponse) =
        weatherLocalData.insertHourlyForecast(hourly)

    suspend fun insertDailyForecast(daily: DailyForecastResponse) =
        weatherLocalData.insertDailyForecast(daily)

    suspend fun getCachedWeather(): WeatherResponse? =
        weatherLocalData.getWeather()

    suspend fun getCachedHourlyForecast(): WeatherForecastResponse? =
        weatherLocalData.getHourlyForecast()

    suspend fun getCachedDailyForecast(): DailyForecastResponse? =
        weatherLocalData.getDailyForecast()
    suspend fun searchLocations(query: String): List<GeoLocation> {
        return locationRemoteDataSource.searchLocations(query)
    }

}

