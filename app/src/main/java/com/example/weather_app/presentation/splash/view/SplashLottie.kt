package com.example.weather_app.presentation.splash.view


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun SplashLottie(scale: Float, alpha: Float) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("rainy_icon.json"))
    val lottieProgress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier
            .size(220.dp)
            .scale(scale)
            .alpha(alpha)
    ) {
        LottieAnimation(
            composition = composition,
            progress = { lottieProgress },
            modifier = Modifier.size(220.dp)
        )
    }
}