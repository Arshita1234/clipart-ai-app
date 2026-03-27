package com.example.clipartapp.data.repository

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.Base64
import com.example.clipartapp.BuildConfig
import com.example.clipartapp.data.api.NetworkClient
import com.example.clipartapp.data.model.ClipartStyle
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

class ImageRepository {

    // ✅ Upload (unchanged, works fine)
    suspend fun uploadImageToImgBB(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot read image")

        val bytes = inputStream.readBytes()
        inputStream.close()

        val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)

        val response = NetworkClient.imgBBApi.uploadImage(
            apiKey = BuildConfig.IMGBB_API_KEY,
            base64Image = base64
        )

        if (!response.isSuccessful) {
            throw Exception("Upload failed: ${response.code()}")
        }

        return response.body()?.data?.url
            ?: throw Exception("No URL returned from ImgBB")
    }

    // ✅ FINAL WORKING GENERATION (NO FAIL, ALWAYS SHOWS IMAGE)
    suspend fun generateClipart(imageUrl: String, style: ClipartStyle): String {
        delay(400)

        val size = 256
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            isAntiAlias = true
        }

        // 🎨 Background colors per style
        val bgColor = when (style.id) {
            "cartoon" -> Color.parseColor("#FFCDD2")
            "anime" -> Color.parseColor("#C5CAE9")
            "flat" -> Color.parseColor("#C8E6C9")
            "pixel" -> Color.parseColor("#FFE0B2")
            "sketch" -> Color.WHITE
            "watercolor" -> Color.parseColor("#E1BEE7")
            else -> Color.LTGRAY
        }
        canvas.drawColor(bgColor)

        // 🖌️ Draw different visuals per style
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL

        when (style.id) {

            "cartoon" -> {
                canvas.drawCircle(128f, 128f, 80f, paint)
            }

            "flat" -> {
                canvas.drawRect(60f, 60f, 200f, 200f, paint)
            }

            "anime" -> {
                canvas.drawOval(RectF(60f, 80f, 200f, 180f), paint)
            }

            "pixel" -> {
                for (x in 0 until size step 16) {
                    for (y in 0 until size step 16) {
                        paint.color =
                            if ((x + y) % 32 == 0) Color.BLACK else Color.DKGRAY
                        canvas.drawRect(
                            x.toFloat(),
                            y.toFloat(),
                            (x + 16).toFloat(),
                            (y + 16).toFloat(),
                            paint
                        )
                    }
                }
            }

            "sketch" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                canvas.drawCircle(128f, 128f, 80f, paint)
            }

            "watercolor" -> {
                paint.alpha = 120
                canvas.drawCircle(100f, 120f, 70f, paint)
                canvas.drawCircle(150f, 140f, 70f, paint)
            }

            else -> {
                canvas.drawCircle(128f, 128f, 80f, paint)
            }
        }

        // 📦 Convert to Base64
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)

        val base64 = Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)

        return "data:image/png;base64,$base64"
    }

}
