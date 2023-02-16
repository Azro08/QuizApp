package com.example.quizapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.R
import com.example.quizapp.adapter.MyRvAdapter
import com.example.quizapp.data.room.entity.Results
import com.example.quizapp.databinding.FragmentResultsBinding
import com.example.quizapp.util.Constants
import com.example.quizapp.viewmodel.ResultsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultsFragment : Fragment() {
    private var _binding : FragmentResultsBinding?=null
    private val binding get() = _binding!!
    private var adapter : MyRvAdapter?=null
    private val viewModel : ResultsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentResultsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val d = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_res_home)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this,d)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getResults.observe(requireActivity()){
            getResults(it)
        }

        binding.btnClearAll.setOnClickListener {
            viewModel.clearAll()
            Toast.makeText(requireContext(), "Cleared", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.nav_res_home)
        }
    }

    private fun getResults(result: List<Results>) {
        adapter = MyRvAdapter(result)
        binding.apply {
            rvResults.layoutManager = LinearLayoutManager(requireContext())
            rvResults.setHasFixedSize(true)
            rvResults.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}