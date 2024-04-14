package com.vujic.rma1.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor {
        val updatedRequest = it.request().newBuilder()
            .addHeader("x-api-key", "live_l3Z3BklHiAUXSzVmWkm6LOi781G2UGCi2hYXWvgP5n4oscZnF0ZhiTvbZhOC4RkZ")
            .build()
        it.proceed(updatedRequest)
    }
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    )
    .build()


val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/v1/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    //.addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
    .build()