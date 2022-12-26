package com.example.qurantutor.viewmodel
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qurantutor.R
import com.example.qurantutor.ResultViewModelFragment
import com.example.qurantutor.data.GetData
import com.example.qurantutor.network.ApiService
import com.example.qurantutor.requests.PostRepo
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class ResultActivityViewModel: ViewModel() {
    val data: MutableLiveData<List<GetData>> = MutableLiveData()
    var errorMssg = ""
    fun getPost() {
        viewModelScope.launch {
            PostRepo.fetchData()
                .catch { e->
                errorMssg = e.message.toString()
                }
                .collect { response->
                    data.value = response.body()

                }
        }
    }






}