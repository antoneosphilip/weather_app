package com.example.weather_app.presentation.splash.view


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.weather_app.R
import com.example.weather_app.ui.theme.primary

@Composable
fun SplashPermissionDialog(
    onGoToSettings: () -> Unit,
    onTryAgain: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        containerColor = primary,
        title = {
            Text(
                text = stringResource(R.string.permission_required_title),
                color = Color.White
            )
        },
        text = {
            Text(
                text = stringResource(R.string.permission_required_settings_message),
                color = Color.White.copy(alpha = 0.8f)
            )
        },
        confirmButton = {
            TextButton(onClick = onGoToSettings) {
                Text(stringResource(R.string.go_to_settings), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onTryAgain) {
                Text(stringResource(R.string.try_again), color = Color.White.copy(alpha = 0.7f))
            }
        }
    )
}