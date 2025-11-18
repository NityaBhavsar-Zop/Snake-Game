package com.example.somegame.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.somegame.data.model.Scores
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(score: Scores)

    @Query("SELECT * FROM Scores ORDER BY score DESC")
    fun getTopScores(): Flow<List<Scores>>
}
