package com.example.weather_app.presentation.location.view

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.LocationSource
import com.example.weather_app.MyApplication
import com.example.weather_app.R
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.example.weather_app.presentation.location.viewModel.LocationPickerState
import com.example.weather_app.presentation.location.viewModel.LocationPickerViewModel
import com.example.weather_app.presentation.location.viewModel.LocationPickerViewModelFactory
import com.example.weather_app.ui.theme.primary
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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

    val appContainer = (context.applicationContext as MyApplication).appContainer

    val locationPickerViewModel: LocationPickerViewModel = viewModel(
        factory = appContainer.locationViewModelFactory
    )
    var selectedPosition by remember {
        mutableStateOf(currentLocation ?: LatLng(30.0444, 31.2357))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedPosition, 12f)
    }

    var searchQuery by remember { mutableStateOf("") }

    val pickerState = locationPickerViewModel.state.value
    val searchResults = if (pickerState is LocationPickerState.SearchResults) pickerState.results else emptyList()
    val isSearching = pickerState is LocationPickerState.Loading

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primary)
            )
        },
        floatingActionButton = {
            currentLocation?.let { location ->
                FloatingActionButton(
                    onClick = {
                        selectedPosition = location
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 12f)
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
                    locationPickerViewModel.clearSearch()
                    searchQuery = ""
                },
                properties = MapProperties(isMyLocationEnabled = currentLocation != null)
            ) {
                Marker(
                    state = MarkerState(position = selectedPosition),
                    title = stringResource(R.string.selected_location),
                    snippet = "Lat: ${selectedPosition.latitude}, Lng: ${selectedPosition.longitude}"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        locationPickerViewModel.searchLocations(it)
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_location),
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        if (isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            if (searchResults.isEmpty()) RoundedCornerShape(12.dp)
                            else RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    singleLine = true
                )

                if (searchResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                            .background(Color.White)
                    ) {
                        items(searchResults) { location ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val latLng = LatLng(location.lat, location.lon)
                                        selectedPosition = latLng
                                        cameraPositionState.position =
                                            CameraPosition.fromLatLngZoom(latLng, 14f)
                                        searchQuery = location.name
                                        locationPickerViewModel.clearSearch()
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = location.name,
                                        color = Color.Black,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${location.state ?: ""} ${location.country}".trim(),
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}