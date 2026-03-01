package com.example.weather_app.presentation.home.view

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.weather_app.LocationSource
import com.example.weather_app.R
import com.example.weather_app.Screens
import com.example.weather_app.presentation.components.CustomLoading
import com.example.weather_app.presentation.components.ErrorMessage
import com.example.weather_app.presentation.components.OfflineBanner
import com.example.weather_app.presentation.home.viewModel.HomeUiState
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.example.weather_app.ui.theme.primary

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = context as Activity

    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.retryLocation(context)
        } else {
            viewModel.onUserDeniedLocation()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.showLocationDialog.collect {
            viewModel.locationProvider.checkLocationSettings(
                activity = activity,
                onSuccess = { viewModel.retryLocation(context) },
                onResolvable = { exception ->
                    locationSettingsLauncher.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )
                },
                onFailed = { viewModel.showLocationError() }
            )
        }
    }

    DisposableEffect(Unit) {
        val receiver = viewModel.locationProvider.registerLocationReceiver {
            viewModel.retryLocation(context)
        }
        viewModel.requestLocationCheck()
        onDispose {
            receiver?.let {
                try {
                    context.unregisterReceiver(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!viewModel.locationProvider.hasPermission()) {
                    viewModel.showLocationError()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { viewModel.shouldNavigateToMap.value }
            .collect { shouldNavigate ->
                if (shouldNavigate) {
                    navController.navigate(Screens.LocationScreen(LocationSource.HOME)) {
                        launchSingleTop = true
                    }
                    viewModel.onNavigatedToMap()
                }
            }
    }

    LaunchedEffect(viewModel.uiState.value) {
        if (viewModel.uiState.value !is HomeUiState.Loading) {
            isRefreshing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        when (val state = viewModel.uiState.value) {
            is HomeUiState.Loading -> CustomLoading()

            is HomeUiState.Error -> ErrorMessage(
                error = state.message,
                modifier = Modifier.align(Alignment.Center)
            )

            is HomeUiState.Success -> {
                val tempUnit = when (viewModel.temp.value) {
                    "°C" -> stringResource(R.string.unit_celsius)
                    "°F" -> stringResource(R.string.unit_fahrenheit)
                    "K" -> stringResource(R.string.unit_kelvin)
                    else -> viewModel.temp.value
                }
                val windUnit = when (viewModel.windSpeedUnit.value) {
                    "mile" -> stringResource(R.string.unit_mph)
                    else -> stringResource(R.string.unit_ms)
                }

                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        viewModel.latLong?.let {
                            viewModel.getAllWeatherData(it.latitude, it.longitude)
                        } ?: run {
                            viewModel.retryLocation(context)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            state = pullToRefreshState,
                            isRefreshing = isRefreshing,
                            modifier = Modifier.align(Alignment.TopCenter),
                            containerColor = Color.White,
                            color = primary,
                        )
                    }
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        if (state.isOffline) {
                            OfflineBanner(message = stringResource(R.string.offline_banner_home))
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        WeatherComponent(
                            weather = state.currentWeather,
                            hourlyForecast = state.hourlyForecast,
                            dailyForecast = state.dailyForecast,
                            temperatureUnit = tempUnit,
                            onLocationClick = {
                                navController.navigate(Screens.LocationScreen(LocationSource.HOME))
                            },
                            windUnit,
                        )
                    }
                }
            }
        }
    }
}