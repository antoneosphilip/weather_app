package com.example.weather_app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather_app.data.alert.datasouce.AlertDao
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.data.favorite.datasource.FavoriteDao
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.data.weather.datasource.local.WeatherDao
import com.example.weather_app.data.weather.model.SettingModel

@Database(entities = [SettingModel::class,LocationModel::class,AlertModel::class], version =8)
abstract class DataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao

    abstract fun getFavoriteDao(): FavoriteDao

    abstract fun getAlertDao(): AlertDao


    companion object{
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getInstance (ctx: Context): DataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, DataBase::class.java, "weather_app_db")
                    .build()
                INSTANCE = instance
                instance }
        }
    }

}
