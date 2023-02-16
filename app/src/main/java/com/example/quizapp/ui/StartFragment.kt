package com.example.quizapp.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentStartBinding
import com.example.quizapp.util.Constants

class StartFragment : Fragment() {
    private var _binding : FragmentStartBinding?=null
    private val binding get() = _binding!!
    private val categoryList = ArrayList<String>()
    private val difficultyList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentStartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDifficultySpinner()
        setCategorySpinner()
        setMenu()
        binding.btnStart.setOnClickListener {
            startQuiz()
        }
    }

    private fun setMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.itemRes ->{
                        findNavController().navigate(R.id.nav_start_Res)
                    }
                }
                return true
                // Handle option Menu Here
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun startQuiz() {
        when {
            TextUtils.isEmpty(binding.etName.text.toString().trim { it <= ' ' }) -> {
                binding.etName.error = "Enter a name!!"
            } else -> {
                val user = binding.etName.text.toString()
                val category = binding.spCategory.selectedItem.toString()
                val difficulty = binding.spDifficulty.selectedItem.toString()
                val userBundle = Pair(Constants.USER, user)
                val categoryBundle = Pair(Constants.CATEGORY, category)
                val difficultyBundle = Pair(Constants.DIFFICULTY, difficulty)
                findNavController().navigate(R.id.nav_start_ques, bundleOf(
                    userBundle, difficultyBundle, categoryBundle
                ))
            }
        }
    }

    private fun setCategorySpinner() {
        categoryList.add("Geography")
        categoryList.add("History")
        categoryList.add("Music")
        categoryList.add("Science")
        categoryList.add("Sport and Literature")
        categoryList.add("Society and Culture")
        val myAdapter = ArrayAdapter(
            requireContext(),
            support_simple_spinner_dropdown_item,
            categoryList
        )
        binding.spCategory.adapter = myAdapter
        binding.spCategory.setSelection(0)
    }

    private fun setDifficultySpinner() {
        difficultyList.add("easy")
        difficultyList.add("medium")
        difficultyList.add("hard")
        val myAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            difficultyList
        )
        binding.spDifficulty.adapter = myAdapter
        binding.spDifficulty.setSelection(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}