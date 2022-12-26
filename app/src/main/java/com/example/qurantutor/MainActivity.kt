package com.example.qurantutor

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.qurantutor.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timerTask


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
        val intent = Intent(this, LoginActivity::class.java)
        Timer().schedule(timerTask {
            startActivity(intent)
            finish()
        }, splashTime)
    }
}