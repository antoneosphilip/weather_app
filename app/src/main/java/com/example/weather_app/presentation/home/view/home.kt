package com.example.weather_app.presentation.home.view

import WeatherIcon
import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weather_app.Screens
import com.example.weather_app.presentation.components.CustomLoading
import com.example.weather_app.presentation.components.ErrorMessage
import com.example.weather_app.presentation.home.viewModel.HomeUiState
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.example.weather_app.presentation.home.viewModel.HomeViewModelFactory
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import com.example.weather_app.presentation.setting.viewModel.SettingViewModelFactory
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    settingViewModel: SettingViewModel,
    navController: NavController,
    viewModel: HomeViewModel
) {
    val scrollState = rememberScrollState()


    LaunchedEffect(viewModel.shouldNavigateToMap.value) {
        if (viewModel.shouldNavigateToMap.value) {
            navController.navigate(Screens.LocationScreen) {
                launchSingleTop = true
            }
        }
//        else{
//            Log.i("TAG", "HomeScreen: "+viewModel.isNavBefore.value)
//            if(viewModel.isNavBefore.value){
//                navController.popBackStack()
//                viewModel.isNavBefore.value = false
//            }
//        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        when (val state = viewModel.uiState.value) {

            is HomeUiState.Loading -> {
                CustomLoading()
            }

            is HomeUiState.Error -> {
                ErrorMessage(
                    error = state.message,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is HomeUiState.Success -> {
                val weather = state.currentWeather
                val hourlyForecast = state.hourlyForecast
                val dailyForecast = state.dailyForecast

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    TopBar(cityName = weather.name)

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.clickable {
                            navController.navigate(Screens.LocationScreen)
                        }
                    ) {
                        CurrentWeather(
                            temperature = weather.main.temp.roundToInt(),
                            icon = weather.weather.firstOrNull()?.icon ?: "",
                            feelsLike = weather.main.feelsLike.roundToInt(),
                            timestamp = weather.dt,
                            condition = weather.weather.firstOrNull()?.description ?: "",
                            unit = viewModel.temp.value
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    WeatherDetailsGrid(
                        humidity = weather.main.humidity,
                        windSpeed = weather.wind.speed,
                        pressure = weather.main.pressure,
                        clouds = weather.clouds.all
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HourlyForecast(hourlyForecast)

                    Spacer(modifier = Modifier.height(24.dp))

                    DailyForecast(dailyForecast)

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}