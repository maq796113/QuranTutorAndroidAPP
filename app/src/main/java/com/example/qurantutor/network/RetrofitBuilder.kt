package com.example.qurantutor.network

import androidx.fragment.app.FragmentManager
import com.example.qurantutor.R
import com.example.qurantutor.ResultViewModelFragment
import com.example.qurantutor.data.GetData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BaseURL.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val fastApiService: ApiService = retrofit.create(ApiService::class.java)

}