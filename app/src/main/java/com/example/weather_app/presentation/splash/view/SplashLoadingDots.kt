package com.example.weather_app.presentation.splash.view


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashLoadingDots(modifier: Modifier = Modifier) {
    val dot1Alpha = remember { Animatable(0.3f) }
    val dot2Alpha = remember { Animatable(0.3f) }
    val dot3Alpha = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        while (true) {
            launch { dot1Alpha.animateTo(1f, tween(400)); dot1Alpha.animateTo(0.3f, tween(400)) }
            delay(150)
            launch { dot2Alpha.animateTo(1f, tween(400)); dot2Alpha.animateTo(0.3f, tween(400)) }
            delay(150)
            launch { dot3Alpha.animateTo(1f, tween(400)); dot3Alpha.animateTo(0.3f, tween(400)) }
            delay(800)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(dot1Alpha.value, dot2Alpha.value, dot3Alpha.value).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(alpha)
                    .background(color = Color.White, shape = CircleShape)
            )
        }
    }
}