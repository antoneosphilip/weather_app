package com.example.weather_app.presentation.splash.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.R
import com.example.weather_app.presentation.splash.viewModel.SplashEvent
import com.example.weather_app.presentation.splash.viewModel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    splashViewModel: SplashViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as androidx.activity.ComponentActivity
    val lifecycleOwner = LocalLifecycleOwner.current

    val event by splashViewModel.event.collectAsState()
    var showSettingsDialog by remember { mutableStateOf(false) }
    var waitingForSettings by remember { mutableStateOf(false) }

    fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    val logoScale = remember { Animatable(0.4f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val dotsAlpha = remember { Animatable(0f) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val canAskAgain = ActivityCompat.shouldShowRequestPermissionRationale(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        )
        splashViewModel.onPermissionResult(granted, canAskAgain)
    }

    LaunchedEffect(event) {
        when (event) {
            is SplashEvent.RequestPermission -> {
                splashViewModel.clearEvent()
                permissionLauncher.launch(locationPermissions)
            }
            is SplashEvent.NavigateToHome -> {
                splashViewModel.clearEvent()
                onSplashFinished()
            }
            is SplashEvent.ShowSettingsDialog -> {
                splashViewModel.clearEvent()
                showSettingsDialog = true
            }
            is SplashEvent.ShowToastAndRetry -> {
                splashViewModel.clearEvent()
                Toast.makeText(context, context.getString(R.string.permission_required_toast), Toast.LENGTH_LONG).show()
                permissionLauncher.launch(locationPermissions)
            }
            null -> {}
        }
    }

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && waitingForSettings) {
                waitingForSettings = false
                splashViewModel.onReturnFromSettings(hasLocationPermission())
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    LaunchedEffect(Unit) {
        launch { logoScale.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing)) }
        launch { logoAlpha.animateTo(1f, animationSpec = tween(600)) }
        delay(400)
        launch { textAlpha.animateTo(1f, animationSpec = tween(600)) }
        delay(700)
        launch { subtitleAlpha.animateTo(1f, animationSpec = tween(500)) }
        launch { dotsAlpha.animateTo(1f, animationSpec = tween(500)) }
        delay(2300)
        splashViewModel.onAnimationFinished(hasLocationPermission())
    }

    if (showSettingsDialog) {
        SplashPermissionDialog(
            onGoToSettings = {
                showSettingsDialog = false
                waitingForSettings = true
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            onTryAgain = {
                showSettingsDialog = false
                splashViewModel.onRetryFromDialog()
            }
        )
    }

    SplashBackground {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SplashLottie(
                scale = logoScale.value,
                alpha = logoAlpha.value
            )

            Spacer(modifier = Modifier.height(24.dp))

            SplashTexts(
                titleAlpha = textAlpha.value,
                subtitleAlpha = subtitleAlpha.value
            )

            Spacer(modifier = Modifier.height(60.dp))

            SplashLoadingDots(modifier = Modifier.alpha(dotsAlpha.value))
        }
    }
}