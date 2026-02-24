package com.example.weather_app.data.alert.datasouce

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.db.DataBase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AlertLocalDataBaseTest {
    @get:Rule
    var instantExecutorRule= InstantTaskExecutorRule()

    private lateinit var database: DataBase
    private lateinit var localDataSource: AlertLocalDataBase
    private lateinit var dao: AlertDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DataBase::class.java
        ).allowMainThreadQueries()
            .build()

        dao=database.getAlertDao()
        localDataSource = AlertLocalDataBase(dao)
    }

    @After
    fun close() {
        database.close()
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
        val result = localDataSource.saveAlert(alert)

        // then get result
        assertTrue(result > 0)
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

        localDataSource.saveAlert(alert)

        // then get alert
        val result = localDataSource.getAlert().first()

       assertThat(alert,`is`(result[0]))
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
       localDataSource.saveAlert(alert)

       // when delete from db
       localDataSource.deleteAlert(alert.id)

       // then get alert
        val result = localDataSource.getAlert().first()
        assertThat(result,`is`(emptyList()))
    }
}