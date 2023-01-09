package com.example.qurantutor.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilderFastApi {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BaseURL.modelbaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val fastApiService: ApiService = retrofit.create(ApiService::class.java)

}