package com.example.clipartapp.data.api

import com.example.clipartapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {

    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // Replicate client (kept for reference)
    private val replicateClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val key = BuildConfig.REPLICATE_API_KEY
            android.util.Log.d("NETWORK", "Replicate key starts with: ${key.take(8)}")
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", "Token $key")
                    .addHeader("Content-Type", "application/json")
                    .build()
            )
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // ImgBB client
    private val imgbbClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // HuggingFace client
    private val huggingFaceClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val key = BuildConfig.HUGGINGFACE_API_KEY
            android.util.Log.d("HF_KEY", "Length=${key.length} starts=${key.take(10)}")
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $key")
                    .build()
            )
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(180, TimeUnit.SECONDS)
        .build()

    val replicateApi: ReplicateApi = Retrofit.Builder()
        .baseUrl("https://api.replicate.com/v1/")
        .client(replicateClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ReplicateApi::class.java)

    val imgBBApi: ImgBBApi = Retrofit.Builder()
        .baseUrl("https://api.imgbb.com/1/")
        .client(imgbbClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ImgBBApi::class.java)

    val huggingFaceApi: HuggingFaceApi = Retrofit.Builder()
        .baseUrl("https://api-inference.huggingface.co/")
        .client(huggingFaceClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HuggingFaceApi::class.java)
}