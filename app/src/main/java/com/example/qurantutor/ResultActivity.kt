package com.example.qurantutor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.qurantutor.databinding.ActivityResultBinding
import com.example.qurantutor.viewmodel.ResultActivityViewModel
import java.io.File

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val filename = intent.getStringExtra("fileName")
        viewModel = ViewModelProvider(this)[ResultActivityViewModel::class.java]
        viewModel.getPost(filename)
        viewModel.errorMssg
        viewModel.data.observe(this, Observer { data ->
            Toast.makeText(this, data[0].language, Toast.LENGTH_LONG).show()
            if (viewModel.error || data[0].language != "ar") {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view_tag, ResultViewModelFragmentError())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view_tag, ResultViewModelFragmentNoError())
                    .commit()
            }

        })
        val filePath = intent.getStringExtra("filePath")
        val file = File(filePath!!)
        val deleted = file.delete()
        if (!deleted) {
            Toast.makeText(this, "App was unable to delete the audio file automatically", Toast.LENGTH_SHORT).show()
        }

    }
}


