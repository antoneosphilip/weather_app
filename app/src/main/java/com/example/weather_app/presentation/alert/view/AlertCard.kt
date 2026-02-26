package com.example.weather_app.presentation.alert.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.ui.theme.Muted
import com.example.weather_app.ui.theme.NavyBorder
import com.example.weather_app.ui.theme.NavyCard
import com.example.weather_app.ui.theme.accent
import com.example.weather_app.ui.theme.primary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AlertCard(
    alert: AlertModel,
    onDelete: () -> Unit,
    onToggle: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    val isActive = alert.isActive

    val format = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val formattedStart = remember(alert.startTime) { format.format(Date(alert.startTime)) }
    val formattedEnd = remember(alert.endTime) {
        alert.endTime?.let { format.format(Date(it)) } ?: "--:--"
    }

    val pulse = rememberInfiniteTransition(label = "pulse")
    val dotAlpha by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = EaseInOutSine),
            RepeatMode.Reverse
        ),
        label = "dot"
    )

    val accentColor by animateColorAsState(
        targetValue = if (isActive) accent else Muted,
        animationSpec = tween(400),
        label = "accent"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .drawBehind {
                drawRect(
                    color = if (isActive) accent else Muted,
                    topLeft = Offset.Zero,
                    size = size.copy(width = 4.dp.toPx())
                )
            }
            .background(
                Brush.horizontalGradient(
                    colors = listOf(NavyCard.copy(alpha = 0.0f), NavyCard),
                    startX = 8f
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.35f),
                        NavyBorder.copy(alpha = 0.0f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = formattedStart,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.White else Color.White.copy(alpha = 0.45f),
                        textDecoration = if (!isActive) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Text(
                        text = "  →  ",
                        fontSize = 13.sp,
                        color = accentColor.copy(alpha = 0.7f)
                    )

                    Text(
                        text = formattedEnd,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.White else Color.White.copy(alpha = 0.45f),
                        textDecoration = if (!isActive) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                Spacer(Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(accentColor.copy(alpha = 0.12f))
                            .border(
                                0.5.dp,
                                accentColor.copy(alpha = 0.4f),
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = alert.type,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accentColor
                        )
                    }

                    if (isActive) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(RoundedCornerShape(50))
                                .background(accent.copy(alpha = dotAlpha))
                        )
                        Text(
                            text = "Active",
                            fontSize = 11.sp,
                            color = accent.copy(alpha = dotAlpha * 0.9f + 0.1f)
                        )
                    } else {
                        Text(
                            text = "Inactive",
                            fontSize = 11.sp,
                            color = Muted
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Switch(
                    checked = isActive,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = primary,
                        checkedTrackColor = accent,
                        uncheckedThumbColor = Muted,
                        uncheckedTrackColor = NavyBorder
                    ),
                    modifier = Modifier.height(24.dp)
                )

                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Muted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = primary,
            title = {
                Text(
                    text = "Delete Alert",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this alert?",
                    color = Color.White
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDialog = false
                    }
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}