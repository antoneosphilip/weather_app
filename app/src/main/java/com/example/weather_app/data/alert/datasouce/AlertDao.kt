package com.example.weather_app.data.alert.datasouce

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.data.favorite.model.LocationModel
import kotlinx.coroutines.flow.Flow
@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlert(alertMode: AlertModel)

    @Query("SELECT * FROM alerts")
    fun getAlert(): Flow<List<AlertModel>>
}