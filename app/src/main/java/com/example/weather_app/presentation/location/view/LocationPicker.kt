package com.example.weather_app.presentation.location.view

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.weather_app.LocationSource
import com.example.weather_app.R
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.example.weather_app.ui.theme.primary
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen(
    homeViewModel: HomeViewModel,
    currentLocation: LatLng?,
    onLocationSelected: (Double, Double, String) -> Unit,
    onBack: () -> Unit,
    source: LocationSource
) {
    val context = LocalContext.current

    var selectedPosition by remember {
        mutableStateOf(currentLocation ?: LatLng(30.0444, 31.2357))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedPosition, 12f)
    }

    LaunchedEffect(homeViewModel.shouldCloseMap.value && source == LocationSource.HOME) {
        if (homeViewModel.shouldCloseMap.value) {
            homeViewModel.onMapClosed()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.select_location), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            var address = ""
                            try {
                                val results = geocoder.getFromLocation(
                                    selectedPosition.latitude,
                                    selectedPosition.longitude,
                                    1
                                )
                                if (!results.isNullOrEmpty()) {
                                    address = results[0].getAddressLine(0) ?: ""
                                }
                            } catch (e: Exception) {
                                address = ""
                            }
                            onLocationSelected(
                                selectedPosition.latitude,
                                selectedPosition.longitude,
                                address
                            )
                            onBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.confirm),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primary
                )
            )
        },
        floatingActionButton = {
            currentLocation?.let { location ->
                FloatingActionButton(
                    onClick = {
                        selectedPosition = location
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(location, 12f)
                    },
                    containerColor = primary
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.my_location)
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedPosition = latLng
                },
                properties = MapProperties(
                    isMyLocationEnabled = currentLocation != null
                )
            ) {
                Marker(
                    state = MarkerState(position = selectedPosition),
                    title = stringResource(R.string.selected_location),
                    snippet = "Lat: ${selectedPosition.latitude}, Lng: ${selectedPosition.longitude}"
                )
            }
        }
    }
}