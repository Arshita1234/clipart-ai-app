package com.example.clipartapp.data.model

data class GeneratedClipart(
    val style: ClipartStyle,
    val imageUrl: String?,
    val isLoading: Boolean = false,
    val error: String? = null
)