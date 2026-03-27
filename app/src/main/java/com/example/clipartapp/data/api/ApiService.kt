package com.example.clipartapp.data.api

import com.example.clipartapp.data.model.ImgBBResponse
import com.example.clipartapp.data.model.ReplicatePrediction
import com.example.clipartapp.data.model.ReplicateRequest
import retrofit2.http.*

interface ReplicateApi {
    @POST("predictions")
    suspend fun createPrediction(
        @Body request: ReplicateRequest
    ): ReplicatePrediction

    @GET("predictions/{id}")
    suspend fun getPrediction(
        @Path("id") id: String
    ): ReplicatePrediction
}

interface ImgBBApi {
    @FormUrlEncoded
    @POST("upload")
    suspend fun uploadImage(
        @Query("key") apiKey: String,
        @Field("image") base64Image: String
    ): retrofit2.Response<ImgBBResponse>
}