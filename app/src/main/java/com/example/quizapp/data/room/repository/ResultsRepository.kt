package com.example.quizapp.data.room.repository

import androidx.lifecycle.LiveData
import com.example.quizapp.data.room.dao.ResultsDao
import com.example.quizapp.data.room.entity.Results
import javax.inject.Inject

class ResultsRepository
@Inject constructor (private val dao: ResultsDao) {
    val getResults : LiveData<List<Results>> = dao.getResult()

    suspend fun insertResult (result: Results){
        dao.insertResult(result)
    }

    suspend fun clearAll(){
        dao.clearAll()
    }

}