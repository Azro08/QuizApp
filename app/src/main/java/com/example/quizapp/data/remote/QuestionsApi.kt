package com.example.quizapp.data.remote

import com.example.quizapp.models.QuestionsResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionsApi {
    @GET("questions")
    suspend fun getQuestions(
        @Query("questions") category : String,
        @Query("limit") limit : String = "10",
        @Query("difficulty") difficulty : String
    ) : Response<List<QuestionsResponseItem>>
}