package com.example.clipartapp.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createImageUri(context: Context): Uri {
    val file = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
    file.parentFile?.mkdirs()
    return FileProvider.getUriForFile(
        context,
        "com.example.clipartapp.provider",  // hardcoded — must match manifest exactly
        file
    )
}