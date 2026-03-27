package com.example.clipartapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    secondary = Pink,
    background = Background,
    surface = Surface,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun ClipartAITheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColorScheme, content = content)
}