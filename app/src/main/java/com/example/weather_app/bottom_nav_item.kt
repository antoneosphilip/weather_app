package com.example.weather_app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screens,
    val icon: ImageVector,
    val title: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screens.HomeScreen, Icons.Default.Home, "Home"),
    BottomNavItem(Screens.FavoriteScreen, Icons.Default.Favorite, "Favorites"),
    BottomNavItem(Screens.AlertScreen, Icons.Default.Notifications, "Alerts"),
    BottomNavItem(Screens.SettingScreen, Icons.Default.Settings, "Settings")
)