package com.example.qurantutor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.core.content.res.ResourcesCompat
import com.example.qurantutor.databinding.ActivityDashboardBinding
import com.example.qurantutor.globalSingleton.Singleton
import com.example.qurantutor.mongodb.Auth
import com.example.qurantutor.mongodb.Auth.user
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.Credentials

import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    @Inject
    lateinit var singleton: Singleton
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val viewb = binding.root
        setContentView(viewb)
        loginAsync(Auth.credentials)
        binding.welcomeTXT.text = String.format(resources.getString(R.string.welcome), singleton.username.substringBefore("@"))


        if (!singleton.isMale) {
            binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.arab_woman, theme))
        }
        val menuDrawer = binding.menuDrawer

        menuDrawer.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            singleton.surahID = position
            if (position in 1..92 || position==95 || position in 99..100 || position==103) {
                return@OnItemClickListener
            }
            val intent = Intent(this, RecitationActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun loginAsync(credentials: Credentials) {
        Auth.app.login(credentials)
        val realmUser = Auth.app.currentUser
        val mongoClient = realmUser





}