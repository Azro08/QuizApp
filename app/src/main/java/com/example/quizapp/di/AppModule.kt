package com.example.quizapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.quizapp.util.Constants
import com.example.quizapp.data.remote.QuestionsApi
import com.example.quizapp.data.remote.QuestionsRepository
import com.example.quizapp.data.room.MyDataBase
import com.example.quizapp.data.room.dao.ResultsDao
import com.example.quizapp.data.room.repository.ResultsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBaseUrl() : String = Constants.BASE_URL


    @Singleton
    @Provides
    fun provideRetrofit(BASE_URL : String) : Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideQuestionsRetrofitInstance (retrofit: Retrofit.Builder) : QuestionsApi =
        retrofit
            .build()
            .create(QuestionsApi::class.java)

    @Singleton
    @Provides
    fun provideQuestionsRepository (api : QuestionsApi) : QuestionsRepository =
        QuestionsRepository(api)

    @Singleton
    @Provides
    fun provideDB (@ApplicationContext app : Context) : MyDataBase =
        Room.databaseBuilder(
            app,
            MyDataBase::class.java,
            MyDataBase.dbName
        ).build()

    @Singleton
    @Provides
    fun provideResultsDao (db : MyDataBase) : ResultsDao =
        db.getResultDao()


    @Singleton
    @Provides
    fun provideResultsRepository (dao: ResultsDao) : ResultsRepository =
        ResultsRepository(dao)

}