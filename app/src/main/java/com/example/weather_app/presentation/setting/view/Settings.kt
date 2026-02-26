package com.example.weather_app.presentation.setting.view

import android.app.Activity
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import com.example.weather_app.presentation.setting.viewModel.LocaleHelper
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel

@Composable
fun SettingsScreen(viewModel: SettingViewModel) {

    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val selectedTemperature by viewModel.selectedTemperature.collectAsState()
    val selectedWindSpeed by viewModel.selectedWindSpeed.collectAsState()
    val context = LocalContext.current

    val languageOptions = mapOf(
        "en" to stringResource(R.string.english),
        "ar" to stringResource(R.string.arabic)
    )
    val locationOptions = mapOf(
        "GPS"    to stringResource(R.string.gps),
        "Manual" to stringResource(R.string.manual)
    )
    val temperatureOptions = mapOf(
        "metric"   to stringResource(R.string.celsius),
        "imperial" to stringResource(R.string.fahrenheit),
        "standard" to stringResource(R.string.kelvin)
    )
    val windSpeedOptions = mapOf(
        "meter" to stringResource(R.string.meter_per_sec),
        "mile"  to stringResource(R.string.mile_per_hour)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.settings),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp, top = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        SettingsSection(
            title = stringResource(R.string.location_settings),
            options = locationOptions.values.toList(),
            selectedOption = locationOptions[selectedLocation] ?: "",
            onOptionSelected = { display ->
                val code = locationOptions.entries.first { it.value == display }.key
                viewModel.changeLocation(code)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(
            title = stringResource(R.string.temperature_unit),
            options = temperatureOptions.values.toList(),
            selectedOption = temperatureOptions[selectedTemperature] ?: "",
            onOptionSelected = { display ->
                val code = temperatureOptions.entries.first { it.value == display }.key
                viewModel.changeTemperature(code)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(
            title = stringResource(R.string.wind_speed_unit),
            options = windSpeedOptions.values.toList(),
            selectedOption = windSpeedOptions[selectedWindSpeed] ?: "",
            onOptionSelected = { display ->
                val code = windSpeedOptions.entries.first { it.value == display }.key
                viewModel.changeWindSpeed(code)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(
            title = stringResource(R.string.language),
            options = languageOptions.values.toList(),
            selectedOption = languageOptions[selectedLanguage] ?: "",
            onOptionSelected = { display ->
                val code = languageOptions.entries.first { it.value == display }.key
                viewModel.changeLanguage(code)
                LocaleHelper.setLocale(context, code)
                (context as Activity).recreate()
            }
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}