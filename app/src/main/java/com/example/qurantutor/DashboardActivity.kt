package com.example.qurantutor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.qurantutor.databinding.ActivityDashboardBinding
import com.example.qurantutor.globalSingleton.Singleton
import com.example.qurantutor.mongodb.Auth
import com.example.qurantutor.mongodb.BaseRealmObject
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    @Inject
    lateinit var singleton: Singleton
    private lateinit var user: User
    private lateinit var binding: ActivityDashboardBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val viewb = binding.root
        setContentView(viewb)
        GlobalScope.launch {
            asyncLogin(Auth.credentials)
        }
        val config = SyncConfiguration.Builder(
            user,
            setOf(BaseRealmObject::class)
        ).initialSubscriptions {realm->
            add(
                realm.query(
                    "owner_id == $0",
                        user.id
                        ),
                "Reciter's Profile"
            )
        }.build()
        val realm = Realm.open(config)
        val profiles: RealmResults<BaseRealmObject> = realm.query<BaseRealmObject>("username = '${singleton.username}'").find()
        val profileList = realm.copyFromRealm(profiles)
        val adapter = RecitersAdapter(profileList)

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

    private suspend fun asyncLogin(credentials: Credentials) {
        user = Auth.app.login(credentials = credentials)
    }


}


