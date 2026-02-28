package com.example.weather_app.presentation.favorite.viewModel

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.data.favorite.model.LocationModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModelTest {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var repo: WeatherRepo
    private lateinit var context: Context

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        repo = mockk()
        context = mockk(relaxed = true)
        coEvery { repo.getLocation() } returns flowOf(emptyList())
        viewModel = FavoriteViewModel(context = context, weatherRepo = repo)
    }


    @Test
    fun saveLocation_success_repoCalledWithCorrectLocation() = runTest {
        // Given new location
        val fakeLocation = LocationModel(
            id = 1,
            lat = 30.0444,
            long = 31.2357,
            location = "Cairo"
        )
        coEvery { repo.saveLocation(fakeLocation) } just runs

        // When save location
        viewModel.saveLocation(fakeLocation)

        // then get location from states and state should be success
        coVerify { repo.saveLocation(fakeLocation) }
        val state = viewModel.favoriteStates.value

        val success = state as FavoriteUiState.SaveSuccess
        assertThat(success.favoriteModel,`is`(fakeLocation))

    }

    @Test
    fun saveLocation_failure_updatesUiStateToError() = runTest {
        // Given a new location
        val fakeLocation = mockk<LocationModel>(relaxed = true)
        coEvery { repo.saveLocation(fakeLocation) } throws Exception("Save failed")

        // When save location
        viewModel.saveLocation(fakeLocation)

        // Then state should be error
        val state = viewModel.favoriteStates.value
        assertTrue(state is FavoriteUiState.Error)
        assertEquals("Save failed", (state as FavoriteUiState.Error).message)
    }


    @Test
    fun deleteLocation_success_repoCalledWithCorrectId() = runTest {

        // Given new location
        val fakeLocation = LocationModel(
            id = 1,
            lat = 30.0444,
            long = 31.2357,
            location = "Cairo"
        )
        coEvery { repo.saveLocation(fakeLocation) } just runs
        viewModel.saveLocation(fakeLocation)

        // When delete location
        coEvery { repo.deleteLocation(fakeLocation.id) } just runs
        viewModel.deleteLocation(fakeLocation.id)

        // then state should be success
        assertTrue(viewModel.favoriteStates.value is FavoriteUiState.DeleteSuccess)
    }

    @Test
    fun deleteLocation_failure_updatesUiStateToError() = runTest {
        // Given location id
        val locationId = 1
        coEvery { repo.deleteLocation(locationId) } throws Exception("Delete failed")

        // When delete location
        viewModel.deleteLocation(locationId)

        // Then the sate should be error
        val state = viewModel.favoriteStates.value
        assertTrue(state is FavoriteUiState.Error)
        assertEquals("Delete failed", (state as FavoriteUiState.Error).message)
    }


    @Test
    fun getLocation_success_updatesUiStateToSuccess() = runTest {
        // Given new location
        val fakeLocation = LocationModel(
            id = 1,
            lat = 30.0444,
            long = 31.2357,
            location = "Cairo"
        )
        val fakeList= listOf(fakeLocation)
        coEvery { repo.getLocation() } returns flowOf(fakeList)

        // When get location
        viewModel.getLocation()

        // Then state should be success
        val state = viewModel.favoriteStates.value
        assertTrue(state is FavoriteUiState.Success)
        assertEquals(fakeList, (state as FavoriteUiState.Success).favoriteList)
    }

    @Test
    fun getLocation_failure_updatesUiStateToError() = runTest {
        // Given a new location
        coEvery { repo.getLocation() } throws Exception("Network error")

        // When get location
        viewModel.getLocation()

        // Then state should be error
        val state = viewModel.favoriteStates.value
        assertTrue(state is FavoriteUiState.Error)
        assertEquals("Network error", (state as FavoriteUiState.Error).message)
    }
}

