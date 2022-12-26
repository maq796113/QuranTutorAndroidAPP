package com.example.qurantutor.requests


import com.example.qurantutor.data.GetData
import com.example.qurantutor.network.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class PostRepo {
    companion object{
        fun fetchData(): Flow<Response<List<GetData>>> = flow {
            val response = RetrofitBuilder.fastApiService.getItems()
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}