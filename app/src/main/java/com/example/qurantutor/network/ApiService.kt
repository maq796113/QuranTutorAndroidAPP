package com.example.qurantutor.network

import com.example.qurantutor.data.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/result")
    suspend fun getItems(@Query("file_name") fileName: String): Response<List<ResponseData>>
}