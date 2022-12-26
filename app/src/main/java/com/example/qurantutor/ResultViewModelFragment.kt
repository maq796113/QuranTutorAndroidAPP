package com.example.qurantutor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentManager
import com.example.qurantutor.viewmodel.ResultActivityViewModel

class ResultViewModelFragment : Fragment() {

    companion object {
        fun newInstance() = ResultViewModelFragment()
    }

    private lateinit var viewModel: ResultActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result_view_model, container, false)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ResultActivityViewModel::class.java]
        // TODO: Use the ViewModel

    }

}