package com.example.qurantutor

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.qurantutor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashTime: Long = 3000
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.progressBar.max = 1000
        val currentProgress = 950
        ObjectAnimator.ofInt(binding.progressBar, "progress", currentProgress)
            .setDuration(splashTime)
            .start()
        Handler().postDelayed( {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, splashTime)
    }
}