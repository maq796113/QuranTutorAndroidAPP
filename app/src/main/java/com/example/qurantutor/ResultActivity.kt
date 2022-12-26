package com.example.qurantutor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.qurantutor.databinding.ActivityResultBinding
import com.example.qurantutor.viewmodel.ResultActivityViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[ResultActivityViewModel::class.java]
        viewModel.data.observe(this, Observer {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view_tag, FragmentError())

        })


    }
}