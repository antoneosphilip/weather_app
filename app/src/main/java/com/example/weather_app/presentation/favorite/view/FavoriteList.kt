package com.example.weather_app.presentation.favorite.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModel

@Composable
fun FavoriteList(favoriteList:List<LocationModel>,nav:NavController,favoriteViewModel:FavoriteViewModel){

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

         items(favoriteList){
                 locationModel-> FavoriteItem(locationModel=locationModel, nav = nav,favoriteViewModel)
         }

    }

}