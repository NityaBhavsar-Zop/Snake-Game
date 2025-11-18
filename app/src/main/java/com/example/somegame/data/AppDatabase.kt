package com.example.somegame.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.somegame.data.dao.ScoresDao
import com.example.somegame.data.model.Scores

@Database(entities = [Scores::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scoresDao(): ScoresDao
}
