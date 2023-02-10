package com.example.qurantutor

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.qurantutor.adapter.RecitersAdapter
import com.example.qurantutor.data.ReciterProfile
import com.example.qurantutor.databinding.ActivityDashboardBinding
import com.example.qurantutor.globalSingleton.Singleton
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Query
import com.google.cloud.datastore.StructuredQuery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    @Inject
    lateinit var singleton: Singleton
    private lateinit var binding: ActivityDashboardBinding
    private var profiles = mutableListOf<ReciterProfile>()
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val viewb = binding.root
        setContentView(viewb)

//        GlobalScope.launch(Dispatchers.IO) {
//            val datastore = withContext(Dispatchers.IO) {
//                DatastoreOptions.newBuilder().setProjectId("quran-tutor-4ab8a").build().service
//            }
//            val query = Query.newEntityQueryBuilder()
//                .setKind("bleu_score")
//                .setFilter(StructuredQuery.PropertyFilter.eq("username", singleton.username))
//                .build()
//            val results = datastore.run(query)
//
//            for (entity in results.iterator()) {
//                val properties = entity.properties
//                val time = properties["time"]?.get() ?: Instant.now()
//                val username = properties["username"]?.get() ?: ""
//                val bleuscore = properties["bleu_score"]?.get() ?: 0.0f
//                val surahid = properties["surah_id"]?.get() ?: 0
//                val profile = ReciterProfile(time as Instant, username as String,
//                    surahid as Int, bleuscore as Float
//                )
//                profiles.add(profile)
//            }
//            val adapter = RecitersAdapter(profiles)
//            binding.recyclerView.adapter = adapter
//
//
//        }


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




}


