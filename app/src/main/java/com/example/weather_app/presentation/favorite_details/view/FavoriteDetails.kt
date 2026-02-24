package com.example.weather_app.presentation.favorite_details.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.weather_app.Screens
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.presentation.components.CustomLoading
import com.example.weather_app.presentation.components.ErrorMessage
import com.example.weather_app.presentation.favorite.viewModel.FavoriteUiState
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModel
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModelFactory
import com.example.weather_app.presentation.favorite_details.viewModel.FavoriteDetailsUiState
import com.example.weather_app.presentation.favorite_details.viewModel.FavoriteDetailsViewModel
import com.example.weather_app.presentation.favorite_details.viewModel.FavoriteDetailsViewModelFactory
import com.example.weather_app.presentation.home.view.WeatherComponent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavoritesDetailsScreen(lat:Double,  long:Double,weatherRepo: WeatherRepo) {
    val context= LocalContext.current
    val favoriteDetailsViewModel: FavoriteDetailsViewModel = viewModel(
        factory = FavoriteDetailsViewModelFactory(context,lat,long,weatherRepo)
    )
    val scrollState = rememberScrollState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    )

    {
        when(val state =favoriteDetailsViewModel.favoriteStates.value){
            is FavoriteDetailsUiState.Error -> {
                ErrorMessage(error = state.message)
            }
            FavoriteDetailsUiState.Loading -> {
                CustomLoading()
            }
            is FavoriteDetailsUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    WeatherComponent(
                        weather = state.currentWeather,
                        hourlyForecast = state.hourlyForecast,
                        dailyForecast = state.dailyForecast,
                        temperatureUnit = favoriteDetailsViewModel.temp.value
                    ) {

                    }
                }
            }
        }



    }
}
