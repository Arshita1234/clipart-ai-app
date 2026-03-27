package com.example.clipartapp.ui.styles

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.clipartapp.data.model.ALL_STYLES
import com.example.clipartapp.data.model.ClipartStyle
import com.example.clipartapp.ui.theme.*

@Composable
fun StylesScreen(
    imageUrl: String,
    onGenerate: (String, List<ClipartStyle>) -> Unit,
    onBack: () -> Unit
) {
    val selectedStyles = remember { mutableStateListOf<String>() }

    Box(
        Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(Modifier.fillMaxSize()) {

            // Top bar
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text("✨", fontSize = 20.sp)
                Spacer(Modifier.width(8.dp))
                Text("Clipart AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // Photo preview row
                Surface(
                    color = SurfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        coil.compose.AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Your Photo", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text("Ready to transform", color = Color(0xFF888888), fontSize = 13.sp)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Choose Styles", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Pick one or more styles to generate", color = Color(0xFF888888), fontSize = 13.sp)
                    }
                    TextButton(onClick = {
                        selectedStyles.clear()
                        selectedStyles.addAll(ALL_STYLES.map { it.id })
                    }) {
                        Text("Select all", color = Purple)
                    }
                }

                Spacer(Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.heightIn(max = 600.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ALL_STYLES) { style ->
                        val isSelected = selectedStyles.contains(style.id)
                        StyleCard(
                            style = style,
                            isSelected = isSelected,
                            onClick = {
                                if (isSelected) selectedStyles.remove(style.id)
                                else selectedStyles.add(style.id)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                AnimatedVisibility(visible = selectedStyles.isNotEmpty()) {
                    Button(
                        onClick = {
                            val chosen = ALL_STYLES.filter { selectedStyles.contains(it.id) }
                            onGenerate(imageUrl, chosen)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(listOf(Purple, Pink)),
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "✨ Generate ${selectedStyles.size} Style${if (selectedStyles.size > 1) "s" else ""}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StyleCard(style: ClipartStyle, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                2.dp,
                if (isSelected) Purple else Color(0xFF333333),
                RoundedCornerShape(14.dp)
            )
            .background(SurfaceVariant)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        if (isSelected) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(22.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(Purple),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
        Column {
            Text(style.emoji, fontSize = 28.sp)
            Spacer(Modifier.height(8.dp))
            Text(style.name, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(style.description, color = Color(0xFF888888), fontSize = 12.sp)
        }
    }
}