package com.example.weather_app.data.favorite.datasource


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.data.weather.model.SettingModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(locationModel: LocationModel)

    @Query("SELECT * FROM location")
     fun getLocation():Flow<List<LocationModel>>

}


