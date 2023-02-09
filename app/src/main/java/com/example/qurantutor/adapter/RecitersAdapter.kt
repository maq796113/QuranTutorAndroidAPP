package com.example.qurantutor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qurantutor.R
import com.example.qurantutor.data.Firestore


class RecitersAdapter(private val profiles: List<Firestore>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTextView: TextView
        private val surahTextView: TextView
        private val scoreTextView: TextView
        init {
            timeTextView = itemView.findViewById(R.id.time)
            surahTextView = itemView.findViewById(R.id.surah)
            scoreTextView = itemView.findViewById(R.id.score)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.itemView.findViewById<TextView>(R.id.time).text = profile.time.toString()
        holder.itemView.findViewById<TextView>(R.id.surah).text = profile.surahID.toString()
        holder.itemView.findViewById<TextView>(R.id.score).text = profile.bleu_score.toString()
    }


    override fun getItemCount(): Int = profiles.size

}