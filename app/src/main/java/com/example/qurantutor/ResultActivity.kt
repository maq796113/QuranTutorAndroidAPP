package com.example.qurantutor

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.qurantutor.databinding.ActivityResultBinding
import com.example.qurantutor.util.ApiState
import com.example.qurantutor.viewmodel.ResultActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultActivityViewModel
    private var notArabic: Boolean = false
    private var bleuScore: Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[ResultActivityViewModel::class.java]
        val filename = intent.getStringExtra("fileName")
        Log.d("filename", "$filename")
        viewModel.getPost(filename!!)
        viewModel.errorMssg
        lifecycleScope.launchWhenStarted {
            viewModel.observerStateFlow.collect {
                when(it) {
                    is ApiState.Loading-> {
                        loadingAnimation()
                    }
                    is ApiState.Failure-> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view_tag, ResultViewModelFragmentError())
                            .commit()
                    }
                    is ApiState.Success-> {
                        if (!it.data.body()?.language.equals("ar")) {
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

    private fun loadingAnimation() {
        var splashTime = 0
        while (splashTime < 6000) {
            Thread.sleep(100)

            if (splashTime < 2000) {
                setText("Loading.")
            } else if (splashTime < 4000) {
                setText("Loading..")
            } else  {
                setText("Loading...")
            }
            splashTime += 100
        }
    }

    private fun setText(text: String) {
        runOnUiThread {
            val titleText = findViewById<TextView>(R.id.bodyText)
            titleText.text = text
        }
    }

}


