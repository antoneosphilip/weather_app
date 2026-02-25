package com.example.weather_app.data.favorite.datasource


import android.content.Context
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.db.DataBase
import kotlinx.coroutines.flow.Flow

class FavoriteLocalDataBase (private val favoriteDao: FavoriteDao ){


    suspend fun saveLocation(locationModel: LocationModel) {
        favoriteDao.saveLocation(locationModel)
    }

     fun getLocation(): Flow<List<LocationModel>> {
       return favoriteDao.getLocation()
    }

  suspend  fun deleteLocation(locationId:Int) {
        return favoriteDao.deleteLocation(locationId)
    }


}