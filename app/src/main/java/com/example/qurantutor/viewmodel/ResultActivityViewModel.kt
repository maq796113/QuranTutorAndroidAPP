package com.example.qurantutor.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qurantutor.requests.PostRepo
import com.example.qurantutor.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultActivityViewModel
@Inject
constructor(private val postRepo: PostRepo): ViewModel() {
    private val postStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val observerStateFlow: StateFlow<ApiState> = postStateFlow
    var error = false
    var errorMssg = String()
    fun getPost(filename: String, username: String, surahId: Int) {
        viewModelScope.launch {
            postStateFlow.value = ApiState.Loading
            postRepo.fetchData(filename, username, surahId)
                .catch { e->
                    postStateFlow.value=ApiState.Failure(e)
                    errorMssg = e.message.toString()
                    error = true
                }
                .collect { response->
                    postStateFlow.value=ApiState.Success(response)
                }

        }
    }
}