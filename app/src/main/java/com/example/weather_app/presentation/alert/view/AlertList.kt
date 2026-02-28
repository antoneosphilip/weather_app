package com.example.weather_app.presentation.alert.view

import AlertsViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.weather_app.data.alert.model.AlertModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weather_app.R
import com.example.weather_app.presentation.components.EmptyState

@Composable
fun AlertList(viewModel: AlertsViewModel, alertList: List<AlertModel>) {
    if (alertList.isEmpty()) {
        EmptyState(
            icon = Icons.Default.NotificationsOff,
            title = stringResource(R.string.no_alerts_title),
            subtitle = stringResource(R.string.no_alerts_subtitle)
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            items(alertList) { alert ->
                AlertCard(
                    alert = alert,
                    onDelete = { viewModel.deleteAlert(alert.id, alert.workId) },
                    onToggle = { viewModel.toggleAlert(alert) }
                )
            }
        }
    }
}