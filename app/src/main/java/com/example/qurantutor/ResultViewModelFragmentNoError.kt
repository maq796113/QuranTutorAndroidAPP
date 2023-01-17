package com.example.qurantutor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.qurantutor.isLoadingSingleton.BooleanSingleton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultViewModelFragmentNoError : Fragment() {
    @Inject
    lateinit var booleanSingleton: BooleanSingleton
    private lateinit var textViewTitle: TextView
    private lateinit var textViewBody: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result_no_error, container, false)
        Log.d("valueOfisLoading", booleanSingleton.isLoading.toString())
        textViewTitle = view.findViewById(R.id.titleText)
        textViewBody = view.findViewById(R.id.bodyText)
        if (booleanSingleton.isLoading) {
            textViewTitle.text = resources.getString(R.string.bleu_score)
            val bleuScore = ((arguments?.getFloat("bleuScore")?.toInt())?.times(100))
            textViewBody.text = getString(R.string.bleu_score_text, bleuScore.toString())
            return view
        }
        return view
    }
}