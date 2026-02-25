package com.example.weather_app.data.alert.datasouce

import android.content.Context
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.db.DataBase
import kotlinx.coroutines.flow.Flow

class AlertLocalDataBase(val alertDao: AlertDao) {

    suspend fun saveAlert( alertMode: AlertModel):Long{
       return alertDao.saveAlert(alertMode)
    }

     fun getAlert(): Flow<List<AlertModel>> {
        return alertDao.getAlert()
    }



    suspend fun deleteAlert(alertId: Int){
         alertDao.deleteAlert(alertId)
    }
}