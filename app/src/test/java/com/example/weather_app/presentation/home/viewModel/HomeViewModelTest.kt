package com.example.weather_app.presentation.home.viewModel

import FakePreferenceStorage
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.weather_app.constant.Constants
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.data.location.ILocationProvider
import com.example.weather_app.data.weather.model.DailyForecastResponse
import com.example.weather_app.data.weather.model.WeatherForecastResponse
import com.example.weather_app.data.weather.model.WeatherResponse
import com.example.weather_app.prefs.PreferenceStorage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class HomeViewModelTest{

    private lateinit var viewModel: HomeViewModel
    private lateinit var repo: WeatherRepo
    private lateinit var prefs: PreferenceStorage
    private lateinit var locationProvider: ILocationProvider
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(UnconfinedTestDispatcher())

        repo = mockk()
        prefs = FakePreferenceStorage()
        locationProvider = FakeLocationProvider()
        coEvery { repo.observeSettings() } returns flowOf(null)

        viewModel = HomeViewModel(
            weatherRepo = repo,
            prefs = prefs,
            locationProvider = locationProvider
        )
    }

    @Test
    fun getAllWeatherData_success_updatesUiStateToSuccess() = runTest {
        // Given fake weather data returned from the repo
        val fakeWeather = mockk<WeatherResponse>(relaxed = true)
        val fakeHourly = mockk<WeatherForecastResponse>(relaxed = true)
        val fakeDaily = mockk<DailyForecastResponse>(relaxed = true)

        val lat = 30.0
        val lon = 31.0
        val lang = "en"
        val unit = "metric"

        coEvery { repo.getWeather(lat, lon, Constants.apiKey, lang, unit) } returns fakeWeather
        coEvery { repo.getHourlyForecast(lat, lon, Constants.apiKey, lang, unit) } returns fakeHourly
        coEvery { repo.getDailyForecast(lat, lon, Constants.apiKey, lang, unit) } returns fakeDaily

        // When fetching all weather data
        viewModel.getAllWeatherData(lat, lon, lang, unit)

        // Then uiState should be Success with the correct data
        assertTrue(viewModel.uiState.value is HomeUiState.Success)
        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(fakeWeather, state.currentWeather)
        assertEquals(fakeHourly, state.hourlyForecast)
        assertEquals(fakeDaily, state.dailyForecast)
    }

    @Test
    fun getAllWeatherData_failure_updatesUiStateToError() = runTest {
        // Given the repo throws an exception when fetching weather
        val lat = 30.0
        val lon = 31.0
        val lang = "en"
        val unit = "metric"

        coEvery { repo.getWeather(lat, lon, Constants.apiKey, lang, unit) } throws Exception("Network error")
        coEvery { repo.getHourlyForecast(lat, lon, Constants.apiKey, lang, unit) } returns mockk(relaxed = true)
        coEvery { repo.getDailyForecast(lat, lon, Constants.apiKey, lang, unit) } returns mockk(relaxed = true)

        // When fetching all weather data
        viewModel.getAllWeatherData(lat, lon, lang, unit)

        // Then uiState should be Error with the correct message
        val state = viewModel.uiState.value
        assertTrue(state is HomeUiState.Error)
        assertEquals("Network error", (state as HomeUiState.Error).message)
    }

    @Test
    fun testGetUnit_metric_returnsCelsius() {
        // Given the unit is metric
        // When getting the unit symbol
        val result = viewModel.getUnit("metric")

        // Then it should return Celsius
        assertEquals("°C", result)
    }

    @Test
    fun testGetUnit_imperial_returnsFahrenheit() {
        // Given the unit is imperial
        // When getting the unit symbol
        val result = viewModel.getUnit("imperial")

        // Then it should return Fahrenheit
        assertEquals("°F", result)
    }

    @Test
    fun testGetUnit_standard_returnsKelvin() {
        // Given the unit is standard
        // When getting the unit symbol
        val result = viewModel.getUnit("standard")

        // Then it should return Kelvin
        assertEquals("K", result)
    }

    @Test
    fun testGetUnit_unknown_returnsEmpty() {
        // Given an unknown unit string
        // When getting the unit symbol
        val result = viewModel.getUnit("random")

        // Then it should return an empty string
        assertEquals("", result)
    }
}