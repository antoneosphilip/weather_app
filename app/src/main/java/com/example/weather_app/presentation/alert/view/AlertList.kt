package com.example.weather_app.presentation.alert.view

import AlertsViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.weather_app.data.alert.model.AlertModel

@Composable
fun AlertList(viewModel: AlertsViewModel, alertList: List<AlertModel>){
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        items(alertList) { alert ->
            AlertCard(
                alert =alert,
                onDelete = { viewModel.deleteAlert(alert.id) },
                onToggle = {viewModel.toggleAlert(alert)}
            )
        }
    }
}