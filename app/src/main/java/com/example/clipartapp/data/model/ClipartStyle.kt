package com.example.clipartapp.data.model

data class ClipartStyle(
    val id: String,
    val name: String,
    val description: String,
    val emoji: String,
    val prompt: String
)

val ALL_STYLES = listOf(
    ClipartStyle("cartoon", "Cartoon", "Bold outlines, vibrant colors", "🎨",
        "cartoon clipart style, bold black outlines, vibrant flat colors, clean illustration"),
    ClipartStyle("flat", "Flat Illustration", "Clean shapes, minimal detail", "🖼️",
        "flat design illustration, minimal shapes, solid colors, modern clipart"),
    ClipartStyle("anime", "Anime", "Japanese animation style", "⛩️",
        "anime style illustration, japanese animation, cel shaded, clean lineart"),
    ClipartStyle("pixel", "Pixel Art", "Retro 8-bit aesthetic", "👾",
        "pixel art style, 8-bit retro game aesthetic, pixelated illustration"),
    ClipartStyle("sketch", "Sketch", "Hand-drawn pencil look", "✏️",
        "pencil sketch illustration, hand drawn, black and white lineart"),
    ClipartStyle("watercolor", "Watercolor", "Soft painterly strokes", "🎭",
        "watercolor painting style, soft edges, painterly, artistic illustration")
)