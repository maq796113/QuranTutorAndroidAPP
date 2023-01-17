package com.example.qurantutor.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitBuilderFastApi {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()

    @Provides
    @Singleton
    fun provideApiService(url: String): ApiService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
}