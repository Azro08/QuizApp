package com.example.quizapp.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quizapp.data.room.entity.Results

@Dao
interface ResultsDao {

    @Insert
    suspend fun insertResult (result: Results)

    @Query("DELETE FROM results_table")
    suspend fun clearAll()

    @Query("SELECT * FROM results_table")
    fun getResult () : LiveData<List<Results>>

}