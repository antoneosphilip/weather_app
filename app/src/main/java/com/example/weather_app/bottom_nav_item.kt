package com.example.weather_app

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screens,
    val icon: ImageVector,
    @StringRes val titleRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem(Screens.HomeScreen, Icons.Default.Home, R.string.nav_home),
    BottomNavItem(Screens.FavoriteScreen, Icons.Default.Favorite, R.string.nav_favorites),
    BottomNavItem(Screens.AlertScreen, Icons.Default.Notifications, R.string.nav_alerts),
    BottomNavItem(Screens.SettingScreen, Icons.Default.Settings, R.string.nav_settings)
)