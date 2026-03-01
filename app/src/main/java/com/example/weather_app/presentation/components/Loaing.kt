package com.example.weather_app.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomLoading() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.05f),
            Color.White.copy(alpha = 0.18f),
            Color.White.copy(alpha = 0.05f),
        ),
        start = Offset(shimmerOffset, shimmerOffset),
        end = Offset(shimmerOffset + 600f, shimmerOffset + 600f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        ShimmerBox(width = 180.dp, height = 24.dp, brush = shimmerBrush)
        Spacer(modifier = Modifier.height(16.dp))
        ShimmerBox(width = 120.dp, height = 80.dp, brush = shimmerBrush, cornerRadius = 16.dp)
        Spacer(modifier = Modifier.height(12.dp))
        ShimmerBox(width = 200.dp, height = 20.dp, brush = shimmerBrush)
        Spacer(modifier = Modifier.height(8.dp))
        ShimmerBox(width = 140.dp, height = 16.dp, brush = shimmerBrush)

        Spacer(modifier = Modifier.height(32.dp))

        ShimmerBox(
            modifier = Modifier.fillMaxWidth(),
            height = 120.dp,
            brush = shimmerBrush,
            cornerRadius = 20.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(4) {
                ShimmerBox(
                    modifier = Modifier.weight(1f),
                    height = 80.dp,
                    brush = shimmerBrush,
                    cornerRadius = 16.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        repeat(3) {
            ShimmerBox(
                modifier = Modifier.fillMaxWidth(),
                height = 60.dp,
                brush = shimmerBrush,
                cornerRadius = 16.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ShimmerBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp,
    brush: Brush,
    cornerRadius: Dp = 12.dp
) {
    val baseModifier = if (width != null) modifier.width(width) else modifier
    Box(
        modifier = baseModifier
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.White.copy(alpha = 0.07f))
            .background(brush)
    )
}