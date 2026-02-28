package com.example.weather_app.presentation.favorite.view

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weather_app.LocationSource
import com.example.weather_app.R
import com.example.weather_app.Screens
import com.example.weather_app.presentation.components.CustomLoading
import com.example.weather_app.presentation.components.ErrorMessage
import com.example.weather_app.presentation.favorite.viewModel.FavoriteUiState
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModel

@Composable
fun FavoritesScreen(nav: NavHostController, favoriteViewModel: FavoriteViewModel) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column {
            when (val state = favoriteViewModel.favoriteStates.value) {
                is FavoriteUiState.Error -> {
                    ErrorMessage(error = state.message)
                }

                is FavoriteUiState.Loading -> {
                    CustomLoading()
                }

                is FavoriteUiState.Success -> {
                    Column(
                        modifier = Modifier.padding(top = 30.dp, start = 30.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.favorites),
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    FavoriteList(favoriteList = state.favoriteList, nav = nav, favoriteViewModel)
                }

                is FavoriteUiState.SaveSuccess -> {
                    print("save success")
                }

                FavoriteUiState.DeleteSuccess -> {
                    print("delete success")
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 30.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable {
                    nav.navigate(Screens.LocationScreen(LocationSource.FAVORITE)) {
                        launchSingleTop = true
                    }
                }
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = stringResource(R.string.favorites),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}