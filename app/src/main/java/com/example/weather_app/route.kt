package com.example.weather_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weather_app.presentation.home.view.HomeScreen
import com.example.weather_app.presentation.setting.view.SettingsScreen
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import com.example.weather_app.presentation.setting.viewModel.SettingViewModelFactory
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(nav: NavHostController,) {
    val context = LocalContext.current

    val settingViewModel: SettingViewModel = viewModel(
        factory = SettingViewModelFactory(context)
    )

    NavHost(
        nav,
       // modifier = Modifier,
        startDestination = Screens.HomeScreen
    ) {

        composable<Screens.HomeScreen> {
            HomeScreen(settingViewModel)
        }
        composable<Screens.AlertScreen> {
            AlertsScreen(nav)

        }
        composable<Screens.SettingScreen> {b->
            SettingsScreen(settingViewModel)

        }
        composable<Screens.FavoriteScreen> {b->
            FavoritesScreen(nav)
        }



    }
}