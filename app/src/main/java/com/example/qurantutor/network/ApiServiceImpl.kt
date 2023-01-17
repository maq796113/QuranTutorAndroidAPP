package com.example.qurantutor.network

import com.example.qurantutor.data.ResponseData
import retrofit2.Response
import javax.inject.Inject


class ApiServiceImpl @Inject constructor(private val apiService: ApiService) {
    suspend fun fetchData(filename: String, username: String, surahId: Int):Response<ResponseData> = apiService.fetchData(filename, username, surahId)
}
