package com.example.qurantutor.network

import com.example.qurantutor.data.ResponseData
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("result")
    suspend fun fetchData(@Query("file_name") fileName: String, @Query("username") username: String, @Query("surah_id") surahID: Int): Response<ResponseData>
}