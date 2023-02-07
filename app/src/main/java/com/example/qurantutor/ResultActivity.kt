package com.example.qurantutor

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.qurantutor.databinding.ActivityResultBinding
import com.example.qurantutor.globalSingleton.Singleton
import com.example.qurantutor.util.ApiState
import com.example.qurantutor.viewmodel.ResultActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultActivityViewModel
    private var notArabic = false

    @Inject
    lateinit var singleton: Singleton
    private var bleuScore: Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[ResultActivityViewModel::class.java]
        val filename = intent.getStringExtra("fileName")
        Log.d("filename", filename.toString())
        viewModel.getPost(filename!!, singleton.username, singleton.surahID+1)
        viewModel.errorMssg
        lifecycleScope.launchWhenStarted {
            viewModel.observerStateFlow.collect {
                when(it) {
                    is ApiState.Loading-> {
                        singleton.isLoading = !singleton.isLoading
                    }
                    is ApiState.Failure-> {
                        Log.d("ErrorCommunicatingWithAPI", it.mssg.toString())
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view_tag, ResultViewModelFragmentError())
                            .commit()
                    }
                    is ApiState.Success-> {
                        if (!it.data.body()?.language.equals("ar")) {
                            singleton.isLoading = !singleton.isLoading
                            notArabic = true
                            val errorFragment = ResultViewModelFragmentError()
                            val bundle = Bundle()
                            bundle.putBoolean("notArabic", notArabic)
                            errorFragment.arguments = bundle
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_view_tag, errorFragment)
                                .commit()
                        } else {
                            bleuScore = it.data.body()!!.bleu_score
                            val noErrorFragment = ResultViewModelFragmentNoError()
                            val bundle = Bundle()
                            bundle.putFloat("bleuScore", bleuScore)
                            noErrorFragment.arguments = bundle
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_view_tag, noErrorFragment)
                                .commit()
                        }
                    }
                    else -> {Toast.makeText(this@ResultActivity, "Retrofit Process Empty", Toast.LENGTH_LONG).show()}
                }
            }
        }


//        val filePath = intent.getStringExtra("filePath")
//        val file = File(filePath!!)
//        val deleted = file.delete()
//        if (!deleted) {
//            Toast.makeText(this, "App was unable to delete the audio file automatically", Toast.LENGTH_SHORT).show()
//        }

    }
}


