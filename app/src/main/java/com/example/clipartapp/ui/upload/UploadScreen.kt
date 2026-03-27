package com.example.clipartapp.ui.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.clipartapp.util.createImageUri
import com.example.clipartapp.ui.theme.*

@Composable
fun UploadScreen(
    onNavigateToStyles: (String) -> Unit,
    viewModel: UploadViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    // Camera capture
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraUri?.let { viewModel.onImageSelected(it) }
        }
    }

    // Camera permission
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            cameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Badge ──────────────────────────────────────
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Purple.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Purple.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✨", fontSize = 14.sp)
                    Spacer(Modifier.width(6.dp))
                    Text("AI-Powered", color = Purple, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Headline ───────────────────────────────────
            Text(
                "Turn photos into",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "stunning clipart",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(
                    brush = Brush.horizontalGradient(listOf(Purple, Pink))
                )
            )

            Spacer(Modifier.height(10.dp))

            Text(
                "Upload your image and generate beautiful\nclipart in multiple artistic styles",
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(32.dp))

            // ── Upload Zone ────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        width = if (state.selectedImageUri != null) 2.dp else 1.dp,
                        color = if (state.selectedImageUri != null) Purple else CardBorder,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(SurfaceVariant)
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (state.selectedImageUri != null) {
                    AsyncImage(
                        model = state.selectedImageUri,
                        contentDescription = "Selected image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f)),
                                    startY = 300f
                                )
                            )
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Text(
                            "✅  Image ready for transformation",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 14.dp)
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    Brush.verticalGradient(listOf(Purple, Pink))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⬆️", fontSize = 30.sp)
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Drop your photo here",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "JPG, PNG or WebP · Max 10MB",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Gallery + Camera ───────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f).height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceVariant),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("🖼️  Gallery", color = Color.White, fontSize = 15.sp)
                }
                Button(
                    onClick = {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.weight(1f).height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceVariant),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("📷  Camera", color = Color.White, fontSize = 15.sp)
                }
            }

            // ── Error ──────────────────────────────────────
            if (state.error != null) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    color = Color(0xFFFF4444).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFFF4444).copy(alpha = 0.4f))
                ) {
                    Text(
                        "❌  ${state.error}",
                        color = Color(0xFFFF6666),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── CTA Button ─────────────────────────────────
            AnimatedVisibility(
                visible = state.selectedImageUri != null,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.horizontalGradient(listOf(Purple, Pink)))
                        .clickable(enabled = !state.isUploading) {
                            viewModel.uploadImage(context) { url ->
                                onNavigateToStyles(url)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isUploading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(26.dp),
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            "Choose Styles →",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Style icons ────────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                listOf("🎨", "🖼️", "⛩️", "👾", "✏️", "🎭").forEach {
                    Text(it, fontSize = 22.sp)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text("6 unique styles available", color = TextSecondary, fontSize = 12.sp)
        }
    }
}