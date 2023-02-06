package com.example.qurantutor

import android.animation.ObjectAnimator
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.qurantutor.databinding.ActivityMainBinding
import io.realm.kotlin.Realm
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashTime: Long = 3000
        super.onCreate(savedInstanceState)
        io.realm.Realm.init(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val remoteViews = RemoteViews(packageName, R.layout.notification_layout)
        val builder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.vector_quran)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            .setCustomContentView(remoteViews)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
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