package com.example.vocabulary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    val word: String,
    val mean: String,
    val type: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
