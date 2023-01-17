package com.example.qurantutor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class PopUpAnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        setContentView(R.layout.activity_pop_up_animation)
    }
}