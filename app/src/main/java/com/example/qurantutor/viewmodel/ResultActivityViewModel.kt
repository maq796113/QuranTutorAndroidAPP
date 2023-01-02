package com.example.qurantutor.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qurantutor.data.ResponseData
import com.example.qurantutor.requests.PostRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch

class ResultActivityViewModel: ViewModel() {
    val data: MutableLiveData<List<ResponseData>> = MutableLiveData()
    var error = false
    var errorMssg = String()
    fun getPost(filename: String?) {
        viewModelScope.launch {
            PostRepo.fetchData(filename)
                .catch { e->
                    errorMssg = e.message.toString()
                    error = true
                }
                .collect { response->
                    if (response.isSuccessful) {
                        data.value = response.body()
                    } else {
                        errorMssg = "Server Error"
                        error = true
                    }

                }
        }
    }
}