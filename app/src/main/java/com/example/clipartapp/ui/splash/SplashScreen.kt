package com.example.clipartapp.ui.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clipartapp.ui.theme.Pink
import com.example.clipartapp.ui.theme.Purple
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.5f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(2000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale)
        ) {
            Text(
                text = "✂️",
                fontSize = 64.sp,
                modifier = Modifier.graphicsLayer(alpha = alpha)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Clipart AI",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                style = androidx.compose.ui.text.TextStyle(
                    brush = Brush.horizontalGradient(listOf(Purple, Pink))
                ),
                modifier = Modifier.graphicsLayer(alpha = alpha)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Turn photos into art ✨",
                fontSize = 16.sp,
                color = Color(0xFF888888),
                modifier = Modifier.graphicsLayer(alpha = alpha)
            )
        }
    }
}