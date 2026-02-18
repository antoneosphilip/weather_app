package com.example.weather_app.data.favorite.datasource


import android.content.Context
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.db.DataBase

class FavoriteLocalDataBase (context: Context){

    private val favoriteDao: FavoriteDao = DataBase.getInstance(context).getFavoriteDao()

    suspend fun saveLocation(locationModel: LocationModel) {
        favoriteDao.saveLocation(locationModel)

    }


}