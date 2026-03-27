package com.example.clipartapp.data.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface HuggingFaceApi {
    @POST("models/{modelId}")
    suspend fun generate(
        @Path(value = "modelId", encoded = true) modelId: String,
        @Body request: RequestBody
    ): Response<ResponseBody>
}