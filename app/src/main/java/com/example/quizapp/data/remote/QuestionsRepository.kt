package com.example.quizapp.data.remote

import com.example.quizapp.models.QuestionsResponseItem
import retrofit2.Response
import javax.inject.Inject

class QuestionsRepository
@Inject constructor (private val api : QuestionsApi){
    suspend fun getQuestions(
        category: String,
        limit: String,
        difficulty: String
    ): Response<List<QuestionsResponseItem>> =
        api.getQuestions(category, limit, difficulty)
}