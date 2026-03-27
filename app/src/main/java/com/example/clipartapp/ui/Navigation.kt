package com.example.clipartapp.ui

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.clipartapp.data.model.ALL_STYLES
import com.example.clipartapp.ui.generate.GenerateScreen
import com.example.clipartapp.ui.splash.SplashScreen
import com.example.clipartapp.ui.styles.StylesScreen
import com.example.clipartapp.ui.upload.UploadScreen
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onFinished = {
                navController.navigate("upload") { popUpTo("splash") { inclusive = true } }
            })
        }
        composable("upload") {
            UploadScreen(onNavigateToStyles = { url ->
                val encoded = URLEncoder.encode(url, "UTF-8")
                navController.navigate("styles/$encoded")
            })
        }
        composable("styles/{imageUrl}") { back ->
            val url = URLDecoder.decode(back.arguments?.getString("imageUrl") ?: "", "UTF-8")
            StylesScreen(
                imageUrl = url,
                onGenerate = { imgUrl, styles ->
                    val encoded = URLEncoder.encode(imgUrl, "UTF-8")
                    val styleIds = styles.joinToString(",") { it.id }
                    navController.navigate("generate/$encoded/$styleIds")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("generate/{imageUrl}/{styleIds}") { back ->
            val url = URLDecoder.decode(back.arguments?.getString("imageUrl") ?: "", "UTF-8")
            val styleIds = back.arguments?.getString("styleIds")?.split(",") ?: emptyList()
            val styles = ALL_STYLES.filter { styleIds.contains(it.id) }
            GenerateScreen(
                imageUrl = url,
                styles = styles,
                onBack = { navController.navigate("upload") { popUpTo(0) } }
            )
        }
    }
}