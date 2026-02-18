package com.example.weather_app.presentation.setting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import com.example.weather_app.presentation.setting.viewModel.SettingViewModelFactory

@Composable
fun SettingsScreen(viewModel: SettingViewModel) {

    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedTemperature by viewModel.selectedTemperature.collectAsState()
    val selectedWindSpeed by viewModel.selectedWindSpeed.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp, top = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        SettingsSection(
            title = "Location Settings",
            options = listOf("GPS", "Manual"),
            selectedOption = selectedLocation,
            onOptionSelected = { viewModel.changeLocation(it)}

        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(
            title = "Temperature Unit",
            options = listOf("Celsius (°C)", "Fahrenheit (°F)", "Kelvin (K)"),
            selectedOption =selectedTemperature,
            onOptionSelected = { viewModel.changeTemperature(it)}

        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(
            title = "Wind Speed Unit",
            options = listOf("Meter/Sec", "Mile/Hour"),
            selectedOption = selectedWindSpeed,
            onOptionSelected = { viewModel.changeWindSpeed(it)}

        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(
            title = "Language",
            options = listOf("English", "Arabic"),
            selectedOption = selectedLanguage,
            onOptionSelected = { viewModel.changeLanguage(it) }
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}