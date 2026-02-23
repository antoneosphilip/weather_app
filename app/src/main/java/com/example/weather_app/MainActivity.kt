package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import android.Manifest
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weather_app.ui.theme.Weather_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1001
            )
        }
        enableEdgeToEdge()
        setContent {
            Weather_appTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val selectedScreen = when {
                            currentDestination == Screens.HomeScreen::class.qualifiedName -> Screens.HomeScreen
                            currentDestination == Screens.FavoriteScreen::class.qualifiedName -> Screens.FavoriteScreen
                            currentDestination?.startsWith(Screens.FavoriteDetails::class.qualifiedName ?: "") == true -> Screens.FavoriteScreen
                            currentDestination == Screens.AlertScreen::class.qualifiedName -> Screens.AlertScreen
                            currentDestination == Screens.SettingScreen::class.qualifiedName -> Screens.SettingScreen
                            else -> Screens.HomeScreen
                        }

                        BottomNavigationBar(
                            navController = navController,
                            currentDestination = selectedScreen
                        )
                    }
                ) { innerPadding ->
                    MyApp(
                        nav = navController
                    )
                }
            }
        }
    }
}