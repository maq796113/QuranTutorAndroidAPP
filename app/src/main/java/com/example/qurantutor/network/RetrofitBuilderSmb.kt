package com.example.qurantutor.network



import jcifs.smb.SmbFile
import kotlinx.coroutines.*
import okhttp3.*

import retrofit2.Retrofit



object RetrofitBuilderSmb {
    private val smbCallFactory = SmbCallFactory()
    private val okHttpClient = OkHttpClient.Builder().build()
    private val service: Retrofit = Retrofit.Builder()
        .baseUrl(BaseURL.smbbaseUrl)
        .client(okHttpClient)
        .callFactory(smbCallFactory)
        .build()
    val smbService: SmbService = service.create(SmbService::class.java)

    private val client: (Request) -> Call = ::newCall
    private fun newCall(request: Request): Call {
            val url = request.url().toString()
            val file = SmbFile(url)
            return SmbCall(request, file)
        }

    }





