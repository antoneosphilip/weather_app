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
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(secondary)
            .padding(24.dp)
    ) {
        Text(
            text = "New Alert",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = white
        )
        Text(
            text = "Set your weather notification window",
            fontSize = 13.sp,
            color = white.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TimePickerField(
            label = if (startTime.isEmpty()) "Start time" else startTime,
            onClick = {
                val cal = Calendar.getInstance()
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val amPm = if (hour < 12) "AM" else "PM"
                        val h = if (hour % 12 == 0) 12 else hour % 12
                        onStartTimeChange(String.format("%d:%02d %s", h, minute, amPm))
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                ).show()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TimePickerField(
            label = if (endTime.isEmpty()) "End time" else endTime,
            onClick = {
                val cal = Calendar.getInstance()
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val amPm = if (hour < 12) "AM" else "PM"
                        val h = if (hour % 12 == 0) 12 else hour % 12
                        onEndTimeChange(String.format("%d:%02d %s", h, minute, amPm))
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                ).show()
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Notify me by",
            fontSize = 14.sp,
            color = white.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NotifyTypeButton(
                label = "Alarm",
                selected = selectedType == "Alarm",
                onClick = { onTypeChange("Alarm") }
            )
            NotifyTypeButton(
                label = "Notification",
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
                    text = "Cancel",
                    color = redCancel,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accent)
            ) {
                Text(
                    text = "Save",
                    color = primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
