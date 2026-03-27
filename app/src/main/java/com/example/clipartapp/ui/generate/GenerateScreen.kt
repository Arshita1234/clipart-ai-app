package com.example.clipartapp.ui.generate

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clipartapp.data.model.*
import com.example.clipartapp.ui.theme.*
import kotlinx.coroutines.*
import java.io.File
import java.net.URL

@Composable
fun GenerateScreen(
    imageUrl: String,
    styles: List<ClipartStyle>,
    onBack: () -> Unit,
    viewModel: GenerateViewModel = viewModel()
) {
    val results by viewModel.results.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.generateAll(imageUrl, styles)
    }

    Column(Modifier.fillMaxSize().background(Background)) {

        Row(Modifier.fillMaxWidth().padding(16.dp)) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Text("Clipart AI", color = Color.White, fontWeight = FontWeight.Bold)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(results) { item ->
                ResultCard(item) {
                    scope.launch { downloadImage(context, it, item.style.name) }
                }
            }
        }
    }
}

@Composable
fun ResultCard(item: GeneratedClipart, onDownload: (String) -> Unit) {
    val context = LocalContext.current

    Box(
        Modifier.aspectRatio(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.DarkGray)
    ) {

        if (item.isLoading) {
            Box(Modifier.fillMaxSize().background(Color.Gray))
        } else if (item.imageUrl != null) {

            val bitmap = remember(item.imageUrl) {
                try {
                    val base64 = item.imageUrl.substringAfter("base64,")
                    val bytes = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                } catch (e: Exception) {
                    null
                }
            }

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Row(Modifier.align(Alignment.BottomEnd)) {
                IconButton(onClick = { onDownload(item.imageUrl) }) {
                    Text("⬇️")
                }
                IconButton(onClick = { shareImage(context, item.imageUrl) }) {
                    Text("📤")
                }
            }
        }
    }
}

suspend fun downloadImage(context: Context, url: String, name: String) {
    withContext(Dispatchers.IO) {
        val base64 = url.substringAfter("base64,")
        val bytes = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "clipart_${name}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ClipartAI")
            }
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )

        uri?.let {
            context.contentResolver.openOutputStream(it)?.write(bytes)
        }
    }
}

fun shareImage(context: Context, url: String) {
    val base64 = url.substringAfter("base64,")
    val bytes = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)

    val file = File(context.cacheDir, "share.png")
    file.writeBytes(bytes)

    val uri = androidx.core.content.FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Share"))
}