package com.example.weather_app.presentation.alert.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weather_app.ui.theme.accent
import com.example.weather_app.ui.theme.primary
import com.example.weather_app.ui.theme.white

@Composable
fun NotifyTypeButton(label: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accent)
        ) {
            Text(text = label, color = primary, fontWeight = FontWeight.SemiBold)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, white.copy(alpha = 0.3f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = white)
        ) {
            Text(text = label, color = white.copy(alpha = 0.7f))
        }
    }
}