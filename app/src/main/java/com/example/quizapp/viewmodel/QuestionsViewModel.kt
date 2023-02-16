package com.example.quizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.remote.QuestionsRepository
import com.example.quizapp.data.room.entity.Results
import com.example.quizapp.data.room.repository.ResultsRepository
import com.example.quizapp.models.QuestionsResponseItem
import com.example.quizapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel
@Inject constructor (private val questionsRepository : QuestionsRepository,
                        private val resRepository : ResultsRepository) : ViewModel(){

    private val _response = MutableLiveData<ScreenState<List<QuestionsResponseItem>?>>()
    val responseQuestion : LiveData<ScreenState<List<QuestionsResponseItem>?>> get() = _response

    private fun internetIsConnected(): Boolean {
        return try {
            val command = "ping -c 1 google.com"
            Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }

    fun getQuestions(category : String, limit : String = "10", difficulty : String) = viewModelScope.launch {
        if (internetIsConnected()){
            questionsRepository.getQuestions(category, limit, difficulty).let { response ->
                _response.postValue(ScreenState.Loading(null))
                if (response.isSuccessful){
                    _response.postValue(ScreenState.Success(response.body()!!))
                }
                else{
                    _response.postValue(ScreenState.Error(response.message().toString(), null))
                }
            }
        } else {
            _response.postValue(ScreenState.Error("Check your internet connection!", null))
        }
    }

    fun insertResult(result: Results) {
        viewModelScope.launch {
            resRepository.insertResult(result)
        }
    }

}