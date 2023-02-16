package com.example.quizapp.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "results_table")
data class Results(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val date : String = "",
    val name : String = "",
    val score : String = "",
)
