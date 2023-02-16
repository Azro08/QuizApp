package com.example.quizapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.data.room.entity.Results
import com.example.quizapp.databinding.FragmentQuestionsBinding
import com.example.quizapp.models.QuestionsResponseItem
import com.example.quizapp.util.Constants
import com.example.quizapp.util.ScreenState
import com.example.quizapp.viewmodel.QuestionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class QuestionsFragment : Fragment() {
    private var _binding : FragmentQuestionsBinding?=null
    private val binding get() = _binding!!
    private val viewModel : QuestionsViewModel by viewModels()
    private var index = 0
    private val options = arrayListOf("")
    private var myPick = ""
    var score = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val category = arguments?.getString(Constants.CATEGORY)
        val difficulty= arguments?.getString(Constants.DIFFICULTY)
        val user= arguments?.getString(Constants.USER)

        if (category != null && user != null && difficulty != null){
            val queryCategory = stringToQueryPar(category)
            Log.d("quesPar", "$difficulty,  $queryCategory")
            viewModel.getQuestions(category = queryCategory, difficulty = difficulty)
            viewModel.responseQuestion.observe(requireActivity()){
                getQuestions(it)
            }
        } else Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()

    }

    private fun getQuestions(state: ScreenState<List<QuestionsResponseItem>?>?) {

        when (state){

            is ScreenState.Loading ->{}

            is ScreenState.Success ->{
                if (state.data != null){
                    binding.apply {
                        btnLayout.visibility = View.VISIBLE
                        btnNext.visibility = View.VISIBLE
                        displayQuestions(state.data)
                    }
                }
            }

            is ScreenState.Error ->{
                binding.tvErr.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "error: $state.message", Toast.LENGTH_LONG).show()
            }

            else -> {
                Toast.makeText(requireContext(),"Something went wrong!!", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun getOptions(correctAnswer : String, wrongAnswers : List<String>) {
        options.clear()
        options.add(correctAnswer)
        for (i in wrongAnswers){
            options.add(i)
        }
        options.shuffle()
    }

    private fun displayQuestions(questionsList : List<QuestionsResponseItem>) {
        Log.d("ques", questionsList.toString())
        val curQuestion = questionsList[index]
        getOptions(curQuestion.correctAnswer, curQuestion.incorrectAnswers)
        binding.apply {
            tvQuestion.text = curQuestion.question
            btnOptionA.text = options[0]
            btnOptionB.text = options[1]
            btnOptionC.text = options[2]
            btnOptionD.text = options[3]

            tvQuesNum.text = "Question No.${index+1}"

            btnOptionA.setOnClickListener {
                myPick = btnOptionA.text.toString()
                btnOptionA.setBackgroundResource(R.drawable.btn_bgnd_clcked)
                btnOptionB.setBackgroundResource(R.drawable.btn_bgnd)
                btnOptionC.setBackgroundResource(R.drawable.btn_bgnd)
                btnOptionD.setBackgroundResource(R.drawable.btn_bgnd)
            }

            btnOptionB.setOnClickListener {
                myPick = btnOptionB.text.toString()
                btnOptionB.setBackgroundResource(R.drawable.btn_bgnd_clcked)
                btnOptionA.setBackgroundResource(R.drawable.btn_bgnd)
                btnOptionC.setBackgroundResource(R.drawable.btn_bgnd)
                btnOptionD.setBackgroundResource(R.drawable.btn_bgnd)
            }

            btnOptionC.setOnClickListener {
                myPick = btnOptionC.text.toString()
                btnOptionC.setBackgroundResource(R.drawable.btn_bgnd_clcked)
                btnOptionB.setBackgroundResource(R.drawable.btn_bgnd)
                btnOptionA.setBackgroundResource(R.drawable.btn_bgnd)
                btnOptionD.setBackgroundResource(R.drawable.btn_bgnd)
            }

            btnOptionD.setOnClickListener {
                myPick = btnOptionD.text.toString()
                btnOptionD.setBackgroundResource(R.drawable.btn_bgnd_clcked)
                btnOptionB.setBackgroundResource(R.drawable.btn_bgnd)
                btnOptionC.setBackgroundResource(R.drawable.btn_bgnd)
                btnOptionA.setBackgroundResource(R.drawable.btn_bgnd)
            }

            if (index == questionsList.size-1) btnNext.text = "FINISH"
            btnNext.setOnClickListener{
                if (index != questionsList.size-1) {
                    if (myPick != ""){
                        checkAnswers(curQuestion.correctAnswer)
                        index++
                        myPick = ""
                        btnOptionD.setBackgroundResource(R.drawable.btn_bgnd)
                        btnOptionB.setBackgroundResource(R.drawable.btn_bgnd)
                        btnOptionC.setBackgroundResource(R.drawable.btn_bgnd)
                        btnOptionA.setBackgroundResource(R.drawable.btn_bgnd)
                        displayQuestions(questionsList)
                    } else Toast.makeText(requireContext(), "Pick an answer!", Toast.LENGTH_SHORT).show()
                } else {
                    checkAnswers(curQuestion.correctAnswer)
                    val user= arguments?.getString(Constants.USER)
                    showResult()
                    saveResToDb(user)
                    val handler = Handler(Looper.myLooper()!!)
                    handler.postDelayed(Runnable {
                        findNavController().navigate(R.id.nav_ques_res)
                    }, 3000)
                }
            }

        }
    }

    private fun saveResToDb(user: String?) {
        if (user != null){
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val currentDate = formatter.format(time)
            val myRes = Results(
                name = user,
                score = "$score/10",
                date = currentDate
            )
            viewModel.insertResult(result = myRes)
        } else{
            Toast.makeText(requireContext(), "Error, Cant find user!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showResult() {
        binding.apply {
            btnLayout.visibility = View.GONE
            tvQuestion.visibility = View.GONE
            tvQuesNum.visibility = View.GONE
            tvResul.visibility = View.VISIBLE
            tvResul.text = "Result: \n $score/10"
        }
    }

    private fun checkAnswers(correctAnswer: String) {
        if (myPick == correctAnswer) score++
    }


    private fun stringToQueryPar(par: String): String {
        var result = ""
        for (i in par.indices) {
            if (par[i] != ' ') {
                result += par[i]
            } else {
                result += "_"
            }
        }
        return result.lowercase()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}