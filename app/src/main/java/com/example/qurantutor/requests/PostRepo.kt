package com.example.qurantutor.requests


import com.example.qurantutor.data.ResponseData
import com.example.qurantutor.network.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class PostRepo {
    companion object{
        fun fetchData(query: String?): Flow<Response<List<ResponseData>>> = flow {
            val response = RetrofitBuilder.fastApiService.getItems(query!!)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}