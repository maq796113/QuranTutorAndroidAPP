package com.example.qurantutor.requests


import com.example.qurantutor.data.ResponseData
import com.example.qurantutor.network.ApiServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


class PostRepo
@Inject
constructor(private val apiServiceImpl: ApiServiceImpl) {
    fun fetchData(filename: String, username: String, surahId: Int): Flow<Response<ResponseData>> = flow {
        val response = apiServiceImpl.fetchData(filename, username, surahId)
        emit(response)
    }.flowOn(Dispatchers.IO)
}
