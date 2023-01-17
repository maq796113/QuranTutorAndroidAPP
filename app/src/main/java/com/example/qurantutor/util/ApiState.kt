package com.example.qurantutor.util

import com.example.qurantutor.data.ResponseData
import retrofit2.Response

sealed class ApiState {
    object Loading : ApiState()
    class Failure(val mssg: Throwable) : ApiState()
    class Success(val data: Response<ResponseData>) : ApiState()
    object Empty : ApiState()
}