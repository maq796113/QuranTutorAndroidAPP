package com.example.qurantutor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.qurantutor.viewmodel.ResultActivityViewModel

class ResultViewModelFragmentNoError : Fragment() {
    private lateinit var viewModel: ResultActivityViewModel
    private lateinit var textViewTitle: TextView
    private lateinit var textViewBody: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result_noterror, container, false)
        textViewTitle = view.findViewById(R.id.titleText)
        textViewBody = view.findViewById(R.id.bodyText)
        textViewTitle.text = R.string.bleu_score.toString()
        viewModel.data.observe(viewLifecycleOwner) {
            textViewBody.text = it[0].bleu_score.toString()
        }
        return view
    }
}