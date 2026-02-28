package com.example.weather_app.presentation.favorite.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModel


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.res.stringResource

import com.example.weather_app.R
import com.example.weather_app.presentation.components.EmptyState

@Composable
fun FavoriteList(
    favoriteList: List<LocationModel>,
    nav: NavController,
    favoriteViewModel: FavoriteViewModel,
    isConnected: Boolean
) {
    if (favoriteList.isEmpty()) {
        EmptyState(
            icon = Icons.Default.FavoriteBorder,
            title = stringResource(R.string.no_favorites_title),
            subtitle = stringResource(R.string.no_favorites_subtitle)
        )
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(favoriteList) { locationModel ->
                FavoriteItem(
                    locationModel = locationModel,
                    nav = nav,
                    viewModel = favoriteViewModel,
                    isConnected = isConnected
                )
            }
        }
    }
}