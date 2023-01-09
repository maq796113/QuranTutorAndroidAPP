package com.example.qurantutor.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface SmbService {
    @Multipart
    @POST("/upload")
    fun uploadFile(@Part file: MultipartBody.Part): Call<ResponseBody>
}