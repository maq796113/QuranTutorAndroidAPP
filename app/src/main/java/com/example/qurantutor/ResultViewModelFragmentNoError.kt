package com.example.qurantutor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.qurantutor.viewmodel.ResultActivityViewModel

class ResultViewModelFragmentNoError : Fragment() {
    private lateinit var viewModel: ResultActivityViewModel
    private lateinit var textViewTitle: TextView
    private lateinit var textViewBody: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result_no_error, container, false)
        textViewTitle = view.findViewById(R.id.titleText)
        textViewBody = view.findViewById(R.id.bodyText)
        textViewTitle.text = getString(R.string.bleu_score)
        viewModel = ViewModelProvider(this)[ResultActivityViewModel::class.java]
        viewModel.data.observe(viewLifecycleOwner) {
            Log.d("Bleu Score:", it[0].bleu_score.toString())
            textViewBody.text = it[0].bleu_score.toString()
        }
        return view
    }
}