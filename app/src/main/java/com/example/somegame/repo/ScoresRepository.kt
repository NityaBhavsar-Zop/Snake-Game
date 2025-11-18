package com.example.somegame.repo

import com.example.somegame.data.dao.ScoresDao
import com.example.somegame.data.model.Scores
import javax.inject.Inject

class ScoresRepository @Inject constructor(
    private val dao: ScoresDao
) {
    suspend fun addScore(score: Scores) = dao.insert(score)
    fun getTopScores() = dao.getTopScores()
}
