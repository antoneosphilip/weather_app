package com.example.weather_app.presentation.home.view

import androidx.compose.material.icons.filled.LocationOn
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.example.weather_app.ui.theme.primary
import com.example.weather_app.ui.theme.secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen(
    currentLocation: LatLng?,
    onLocationSelected: (Double, Double) -> Unit,
    onBack: () -> Unit
) {
    var selectedPosition by remember {
        mutableStateOf(currentLocation ?: LatLng(30.0444, 31.2357))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedPosition, 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Location") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onLocationSelected(
                                selectedPosition.latitude,
                                selectedPosition.longitude
                            )
                            onBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirm",
                            tint = MaterialTheme.colorScheme.primary
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
                        contentDescription = "My Location"
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
                    title = "Selected Location",
                    snippet = "Lat: ${selectedPosition.latitude}, Lng: ${selectedPosition.longitude}"
                )
            }

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = secondary
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Selected Location:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Latitude: ${String.format("%.6f", selectedPosition.latitude)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Longitude: ${String.format("%.6f", selectedPosition.longitude)}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onLocationSelected(
                                selectedPosition.latitude,
                                selectedPosition.longitude
                            )
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary
                        )
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}