package com.example.weather_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.presentation.favorite.view.FavoritesScreen
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModel
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModelFactory
import com.example.weather_app.presentation.favorite_details.view.FavoritesDetailsScreen
import com.example.weather_app.presentation.home.view.HomeScreen
import com.example.weather_app.presentation.home.view.LocationPickerScreen
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.example.weather_app.presentation.home.viewModel.HomeViewModelFactory
import com.example.weather_app.presentation.setting.view.SettingsScreen
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import com.example.weather_app.presentation.setting.viewModel.SettingViewModelFactory
import com.google.android.gms.maps.model.LatLng
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

    @Serializable
    data class LocationScreen(
        val locationSource: LocationSource
    ) :Screens()

    @Serializable
    data class FavoriteDetails(
        val lat:Double,
        val long:Double
    ) : Screens()
}
enum class LocationSource {
    HOME,
    FAVORITE
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(nav: NavHostController,) {
    val context = LocalContext.current

    val settingViewModel: SettingViewModel = viewModel(
        factory = SettingViewModelFactory(context)
    )

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(context,settingViewModel)
    )

    val favoriteViewModel: FavoriteViewModel = viewModel(
        factory = FavoriteViewModelFactory(context)
    )


    NavHost(
        nav,
       // modifier = Modifier,
        startDestination = Screens.HomeScreen
    ) {

        composable<Screens.HomeScreen> {
            HomeScreen(nav,homeViewModel)
        }
        composable<Screens.AlertScreen> {
            AlertsScreen(nav)

        }
        composable<Screens.SettingScreen> {b->
            SettingsScreen(settingViewModel)

        }
        composable<Screens.FavoriteScreen> {b->
            FavoritesScreen(nav,favoriteViewModel)
        }


        composable<Screens.FavoriteDetails> { b ->
            val args = b.toRoute<Screens.FavoriteDetails>()
            FavoritesDetailsScreen(args.lat, args.long)
        }
        composable<Screens.LocationScreen> { backStackEntry ->

            val args = backStackEntry.toRoute<Screens.LocationScreen>()
            val source = args.locationSource

            LocationPickerScreen(
                currentLocation = homeViewModel.latLong?.let {
                    LatLng(it.latitude, it.longitude)
                },
                onLocationSelected = { lat, lng, address ->

                    when (source) {

                        LocationSource.HOME -> {
                            homeViewModel.getAllWeatherData(lat, lng)
                        }

                        LocationSource.FAVORITE -> {
                            favoriteViewModel.saveLocation(
                                LocationModel(
                                    lat = lat,
                                    long = lng,
                                    location = address
                                )
                            )
                        }
                    }
                },
                onBack = {
                    nav.popBackStack()
                },
                homeViewModel = homeViewModel,
                source = source
            )
        }
    }
}