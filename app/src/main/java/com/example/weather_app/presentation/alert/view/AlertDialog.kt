package com.example.weather_app.presentation.alert.view

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import com.example.weather_app.ui.theme.accent
import com.example.weather_app.ui.theme.primary
import com.example.weather_app.ui.theme.redCancel
import com.example.weather_app.ui.theme.secondary
import com.example.weather_app.ui.theme.white
import java.util.Calendar

@Composable
fun NewAlertDialog(
    context: Context,
    startTime: String,
    endTime: String,
    selectedType: String,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    val now = Calendar.getInstance()
    val currentHour = now.get(Calendar.HOUR_OF_DAY)
    val currentMinute = now.get(Calendar.MINUTE)

    val startHour: Int
    val startMinute: Int
    if (startTime.isNotEmpty()) {
        val parts = startTime.split(":", " ")
        val isPm = startTime.contains("PM")
        var h = parts[0].toIntOrNull() ?: currentHour
        val m = parts[1].toIntOrNull() ?: currentMinute
        if (isPm && h != 12) h += 12
        if (!isPm && h == 12) h = 0
        startHour = h
        startMinute = m
    } else {
        startHour = currentHour
        startMinute = currentMinute
    }

    val isSaveEnabled = startTime.isNotEmpty() && endTime.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(secondary)
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.new_alert),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = white
        )
        Text(
            text = stringResource(R.string.alert_subtitle),
            fontSize = 13.sp,
            color = white.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TimePickerField(
            label = if (startTime.isEmpty()) stringResource(R.string.start_time) else startTime,
            onClick = {
                val dialog = TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val isAfterNow = hour > currentHour ||
                                (hour == currentHour && minute >= currentMinute)
                        if (isAfterNow) {
                            val amPm = if (hour < 12) "AM" else "PM"
                            val h = if (hour % 12 == 0) 12 else hour % 12
                            onStartTimeChange(String.format("%d:%02d %s", h, minute, amPm))
                        }
                    },
                    currentHour,
                    currentMinute,
                    false
                )
                dialog.show()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TimePickerField(
            label = if (endTime.isEmpty()) stringResource(R.string.end_time) else endTime,
            onClick = {
                val dialog = TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val isAfterStart = hour > startHour ||
                                (hour == startHour && minute > startMinute)
                        if (isAfterStart) {
                            val amPm = if (hour < 12) "AM" else "PM"
                            val h = if (hour % 12 == 0) 12 else hour % 12
                            onEndTimeChange(String.format("%d:%02d %s", h, minute, amPm))
                        }
                    },
                    startHour,
                    startMinute,
                    false
                )
                dialog.show()
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.notify_me_by),
            fontSize = 14.sp,
            color = white.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NotifyTypeButton(
                label = stringResource(R.string.alarm),
                selected = selectedType == "Alarm",
                onClick = { onTypeChange("Alarm") }
            )
            NotifyTypeButton(
                label = stringResource(R.string.notification),
                selected = selectedType == "Notification",
                onClick = { onTypeChange("Notification") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = redCancel),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, redCancel)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = redCancel,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onSave,
                enabled = isSaveEnabled,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text(
                    text = stringResource(R.string.save),
                    color = if (isSaveEnabled) primary else Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}