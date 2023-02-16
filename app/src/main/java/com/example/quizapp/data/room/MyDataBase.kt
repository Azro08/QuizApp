package com.example.quizapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizapp.data.room.dao.ResultsDao
import com.example.quizapp.data.room.entity.Results

@Database(entities = [Results::class], version = 1, exportSchema = false)
abstract class MyDataBase : RoomDatabase() {
    abstract fun getResultDao() : ResultsDao

    companion object{
        @Volatile
        private var database : MyDataBase?= null

        const val dbName = "myDB"

        @Synchronized
        fun getInstance(context: Context) : MyDataBase {
            return if (database == null){
                database = Room.databaseBuilder(context, MyDataBase::class.java, dbName).build()
                database as MyDataBase
            } else {
                database as MyDataBase
            }
        }
    }

}