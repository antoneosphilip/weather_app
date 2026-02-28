package com.example.weather_app.presentation.splash.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.weather_app.ui.theme.primary

@Composable
fun SplashBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primary,
                        Color(0xFF252840),
                        Color(0xFF12142A)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}