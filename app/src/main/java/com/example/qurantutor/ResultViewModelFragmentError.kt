package com.example.qurantutor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.qurantutor.viewmodel.ResultActivityViewModel

class ResultViewModelFragmentError : Fragment() {

    private lateinit var viewModel: ResultActivityViewModel
    private lateinit var textViewTitle: TextView
    private lateinit var textViewBody: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result_error, container, false)
        textViewTitle = view.findViewById(R.id.titleText)
        textViewBody = view.findViewById(R.id.bodyText)
        viewModel = ViewModelProvider(this)[ResultActivityViewModel::class.java]
        val lang = viewModel.data.value?.get(0)?.language
        if (lang != "ar") {
            textViewTitle.text = R.string.wrongLang.toString()
            return view
        }
        textViewBody.text = viewModel.errorMssg
        return view
    }

}