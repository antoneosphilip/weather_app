package com.example.weather_app.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weather_app.data.alert.datasouce.AlertLocalDataBase
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.data.favorite.datasource.FavoriteLocalDataBase
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.data.location.datasource.LocationRemoteDataSource
import com.example.weather_app.data.setting.datasource.SettingLocalDataBase
import com.example.weather_app.data.weather.datasource.local.WeatherLocalDataBase
import com.example.weather_app.data.weather.datasource.remote.WeatherRemoteDataSource
import com.example.weather_app.data.weather.model.WeatherResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class WeatherRepoTest{

    @get:Rule
    var instantExecutorRule= InstantTaskExecutorRule()
    private lateinit var weatherRemoteData: WeatherRemoteDataSource
    private lateinit var favoriteLocalDataBase: FavoriteLocalDataBase
    private lateinit var weatherLocalData: WeatherLocalDataBase
    private lateinit var alertLocalDataBase: AlertLocalDataBase
    private lateinit var settingLocalDataBase: SettingLocalDataBase
    private lateinit var locationRemoteDataSource: LocationRemoteDataSource

    private lateinit var weatherRepo: WeatherRepo

    @Before
    fun setup() {
        weatherRemoteData = mockk()
        favoriteLocalDataBase = mockk()
        weatherLocalData = mockk()
        alertLocalDataBase = mockk()
        settingLocalDataBase= mockk()
        locationRemoteDataSource= mockk()
        weatherRepo = WeatherRepo(
            weatherLocalData,
            favoriteLocalDataBase,
            alertLocalDataBase,
            weatherRemoteData,
            settingLocalDataBase,
            locationRemoteDataSource
        )
    }
    @Test
    fun getWeather_shouldReturnWeatherResponse()=runTest{
            //Given response
            val weatherResponse = mockk<WeatherResponse>()

             coEvery {
            weatherRemoteData.getWeather(1.0, 2.0, "apiKey", "en", "metric")
             } returns weatherResponse

            // When get weather
            val result = weatherRepo.getWeather(1.0, 2.0, "apiKey")

            //then return result
          assertThat(result, `is`(weatherResponse))
    }
    @Test
    fun saveLocation_shouldInsertAndReturnData() = runTest {
        // Given location
        val location = LocationModel(
            id = 1,
            lat = 30.0,
            long = 31.0,
            location = "Cairo"
        )

        val fakeList = listOf(location)

        coEvery {
            favoriteLocalDataBase.saveLocation(location)
        } just runs

        every {
            favoriteLocalDataBase.getLocation()
        } returns flowOf(fakeList)

        // When save location
        weatherRepo.saveLocation(location)
        val result = weatherRepo.getLocation().first()

        // Then return location
        assertThat(result[0], `is`(location))

        coVerify {
            favoriteLocalDataBase.saveLocation(location)
        }
    }
    @Test
    fun deleteAlert_shouldCallDeleteAlert() = runTest {

        // Given new alert
        val alert = AlertModel(
            id = 1,
            startTime = 12345L,
            endTime = 23456L,
            type = "Notification",
            label = "Test Alert",
            isActive = true,
            workId = "work-1"
        )

        coEvery { alertLocalDataBase.saveAlert(alert) } returns alert.id.toLong()

        coEvery { alertLocalDataBase.deleteAlert(alert.id) } just runs

        coEvery { alertLocalDataBase.getAlert() } returns flowOf(emptyList())

        // When delete alarm
        weatherRepo.saveAlert(alert)
        weatherRepo.deleteAlert(alert.id)

        // Then check list
        coVerify { alertLocalDataBase.saveAlert(alert) }
        coVerify { alertLocalDataBase.deleteAlert(alert.id) }

        val result = weatherRepo.getAlert().first()
        assertThat(result, `is`(emptyList()))
    }
}