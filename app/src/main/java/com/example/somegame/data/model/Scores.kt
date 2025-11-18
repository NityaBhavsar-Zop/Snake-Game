package com.example.somegame.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scores (
    @PrimaryKey
    val score: Int = 0
)