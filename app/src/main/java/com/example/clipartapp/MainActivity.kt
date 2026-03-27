package com.example.clipartapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.clipartapp.ui.AppNavigation
import com.example.clipartapp.ui.theme.ClipartAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ClipartAITheme {
                AppNavigation()
            }
        }
    }
}