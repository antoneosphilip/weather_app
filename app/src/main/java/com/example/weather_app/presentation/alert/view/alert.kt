package com.example.weather_app.presentation.alert.view

import AlertsViewModel
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


@Composable
fun AlertsScreen(nav: NavHostController) {

    val viewModel: AlertsViewModel = viewModel()

    val showDialog by viewModel.showDialog.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        Column(
            modifier = Modifier.padding(vertical = 30.dp, horizontal = 30.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = "My Alerts",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "0 active alerts",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.Accent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.acc,
                        modifier = Modifier.size(33.dp)
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.showDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp, 90.dp),
            containerColor = accent,
            contentColor = primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Alert"
            )
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
