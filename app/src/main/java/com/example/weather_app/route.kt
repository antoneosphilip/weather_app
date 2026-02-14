package com.example.weather_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

sealed class Screens{
    @Serializable
    object HomeScreen :Screens()
    @Serializable
    object AlertScreen :Screens()
    @Serializable
    object FavoriteScreen :Screens()
    @Serializable
    object SettingScreen :Screens()
}

@Composable
fun MyApp(nav: NavHostController) {
    NavHost(
        nav,
       // modifier = Modifier,
        startDestination = Screens.HomeScreen
    ) {
        composable<Screens.HomeScreen> {
            HomeScreen()
        }
        composable<Screens.AlertScreen> {
            AlertsScreen(nav)

        }
        composable<Screens.SettingScreen> {b->
            SettingsScreen(nav)

        }
        composable<Screens.FavoriteScreen> {b->
            FavoritesScreen(nav)
        }



    }
}