package com.example.qurantutor.network

import com.example.qurantutor.data.GetData
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("result")
    suspend fun getItems(): Response<List<GetData>>
}