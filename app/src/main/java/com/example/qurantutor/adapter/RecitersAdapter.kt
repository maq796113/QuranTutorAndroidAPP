package com.example.qurantutor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qurantutor.R
import com.example.qurantutor.mongodb.BaseRealmObject

class RecitersAdapter(private val profiles: List<BaseRealmObject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
        val ageTextView: TextView = itemView.findViewById(R.id.age_text_view)
        val speciesTextView: TextView = itemView.findViewById(R.id.species_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_frog, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = profiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val frog = profiles[position]
        holder.nameTextView.text = frog.name
        holder.ageTextView.text = frog.age.toString()
        holder.speciesTextView.text = frog.species
    }

}