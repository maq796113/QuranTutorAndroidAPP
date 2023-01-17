package com.example.qurantutor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import com.example.qurantutor.databinding.ActivityDashboardBinding
import com.example.qurantutor.globalSingleton.Singleton
import dagger.hilt.android.AndroidEntryPoint
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
        val menuDrawer = binding.menuDrawer
        menuDrawer.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position in 2..93 || position==96 || position in 100..101 || position==104) {
                view.isEnabled = false
            }
            singleton.surahID = position
            binding.welcomeTXT.text = singleton.username
            val intent = Intent(this, RecitationActivity::class.java)
            startActivity(intent)
        }
    }
}