package com.example.weather_app.data.alert.datasouce

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.db.DataBase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AlertDaoTest {

    private lateinit var alertDao:AlertDao
    private lateinit var dataBase: DataBase

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()

        dataBase = Room.inMemoryDatabaseBuilder(
            context,
            DataBase::class.java
        ).allowMainThreadQueries()
            .build()

        alertDao=dataBase.getAlertDao()

    }

    @After
    fun close() {
        dataBase.close()
    }


    @Test
    fun saveAlert_shouldInsertAlert() = runTest {

        // Given a new alert
        val alert = AlertModel(
            id = 1,
            startTime = 12345,
            endTime = 12345,
            type="Notification",
        )

        // when inserting into db
        val result = alertDao.saveAlert(alert)

        // then get result
        Assert.assertTrue(result > 0)
    }
    @Test
    fun getAlert_shouldReturnInsertedAlerts() = runTest {
        // Given a new alert

        val alert = AlertModel(
            id = 1,
            startTime = 12345,
            endTime = 12345,
            type="Notification",
        )
        // when inserting into db

        alertDao.saveAlert(alert)

        // then get alert
        val result = alertDao.getAlert().first()

        Assert.assertThat(alert, CoreMatchers.`is`(result[0]))
    }

    @Test
    fun deleteAlert_shouldReturnEmptyAlerts() = runTest {
        // Given a new alert
        val alert = AlertModel(
            id = 1,
            startTime = 12345,
            endTime = 12345,
            type="Notification",
        )
        alertDao.saveAlert(alert)

        // when delete from db
        alertDao.deleteAlert(alert.id)

        // then get alert
        val result = alertDao.getAlert().first()
        Assert.assertThat(result, CoreMatchers.`is`(emptyList()))
    }

}