package com.example.clipartapp.data.model

data class ReplicateRequest(
    val version: String,
    val input: Map<String, Any>
)

data class ReplicatePrediction(
    val id: String,
    val status: String,
    val output: List<String>? = null,
    val error: String? = null
)

data class ImgBBResponse(
    val data: ImgBBData?,
    val success: Boolean
)

data class ImgBBData(val url: String)

