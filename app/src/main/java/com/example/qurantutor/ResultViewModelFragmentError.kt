package com.example.qurantutor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class ResultViewModelFragmentError : Fragment() {



    private lateinit var textViewTitle: TextView
    private lateinit var textViewBody: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result_error, container, false)
        textViewTitle = view.findViewById(R.id.titleText)
        textViewBody = view.findViewById(R.id.bodyText)

        val notArabic = arguments?.getBoolean("notArabic")
        return if (notArabic == true) {
            textViewTitle.text = resources.getString(R.string.wrongLang)
            view
        } else {
            textViewBody.text = resources.getString(R.string.error_505)
            view
        }
    }

}