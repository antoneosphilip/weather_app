package com.example.weather_app.presentation.alert.view

import AlertViewModelFactory
import AlertsViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import android.provider.Settings
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.weather_app.MyApplication
import com.example.weather_app.R
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.presentation.alert.viewModel.AlertUiState
import com.example.weather_app.presentation.components.CustomLoading
import com.example.weather_app.presentation.components.ErrorMessage
import com.example.weather_app.ui.theme.accent
import com.example.weather_app.ui.theme.primary


@Composable
fun AlertsScreen(nav: NavHostController) {
    val context = LocalContext.current

    val application = context.applicationContext as MyApplication
    val viewModel: AlertsViewModel = viewModel(factory = application.appContainer.alertViewModelFactory)

    val showDialog by viewModel.showDialog.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
    ) {

        when (val state = viewModel.alertStates.value) {

            is AlertUiState.Error -> {
                ErrorMessage(error = state.message)
            }

            AlertUiState.Loading -> {
                CustomLoading()
            }

            is AlertUiState.Success -> {

                Column(
                    modifier = Modifier.padding(30.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.my_alerts),
                                fontSize = 24.sp,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.active_alerts, state.alertList.count { it.isActive }),
                                fontSize = 14.sp,
                                color = White.copy(alpha = 0.5f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, accent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = accent,
                                modifier = Modifier.size(33.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    }

                    Spacer(Modifier.height(24.dp))

                    AlertList(
                        viewModel = viewModel,
                        alertList = state.alertList
                    )
                }

                FloatingActionButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val permission = android.Manifest.permission.POST_NOTIFICATIONS
                            if (androidx.core.content.ContextCompat.checkSelfPermission(
                                    context, permission
                                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                            ) {
                                androidx.core.app.ActivityCompat.requestPermissions(
                                    (context as androidx.activity.ComponentActivity),
                                    arrayOf(permission),
                                    1001
                                )
                                return@FloatingActionButton
                            }
                        }

                        if (!Settings.canDrawOverlays(context)) {
                            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                                data = Uri.parse("package:${context.packageName}")
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        } else {
                            viewModel.showDialog()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp, 90.dp),
                    containerColor = accent,
                    contentColor = primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }

                AnimatedVisibility(
                    visible = showDialog,
                    enter = fadeIn() + scaleIn(initialScale = 0.9f),
                    exit = fadeOut() + scaleOut(targetScale = 0.9f)
                ) {
                    Dialog(onDismissRequest = { viewModel.hideDialog() }) {
                        NewAlertDialog(
                            context = context,
                            startTime = startTime,
                            endTime = endTime,
                            selectedType = selectedType,
                            onStartTimeChange = viewModel::updateStartTime,
                            onEndTimeChange = viewModel::updateEndTime,
                            onTypeChange = viewModel::updateType,
                            onCancel = viewModel::hideDialog,
                            onSave = viewModel::saveAlert
                        )
                    }
                }
            }
        }
    }
}