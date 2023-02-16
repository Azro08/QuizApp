package com.example.quizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.room.entity.Results
import com.example.quizapp.data.room.repository.ResultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel
@Inject constructor (private val repository: ResultsRepository) : ViewModel(){
    val getResults = repository.getResults

    fun clearAll(){
        viewModelScope.launch {
            repository.clearAll()
        }
    }

}