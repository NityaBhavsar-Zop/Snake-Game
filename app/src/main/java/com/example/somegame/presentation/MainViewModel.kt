package com.example.somegame.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somegame.data.model.Direction
import com.example.somegame.data.model.GameState
import com.example.somegame.data.model.GameStatus
import com.example.somegame.data.model.Scores
import com.example.somegame.repo.ScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val scoresRepository: ScoresRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val gridSize = 21
    private var gameJob: Job? = null

    fun startGame() {
        _gameState.update {
            it.copy(
                status = GameStatus.IN_PROGRESS,
                foodPosition = spawnFood(it.snakeCoordinates)
            )
        }
        startGameLoop()
    }

    fun restartGame() {
        val initialState = GameState(status = GameStatus.NOT_STARTED)
        _gameState.value = initialState.copy(
            foodPosition = spawnFood(initialState.snakeCoordinates)
        )
        startGameLoop()
    }

    fun changeDirection(newDirection: Direction) {
        // Only allow direction change during gameplay
        if (_gameState.value.status != GameStatus.IN_PROGRESS) return

        val current = _gameState.value.direction

        // Prevent 180-degree turns
        val isOpposite = (current == Direction.UP && newDirection == Direction.DOWN) ||
                (current == Direction.DOWN && newDirection == Direction.UP) ||
                (current == Direction.LEFT && newDirection == Direction.RIGHT) ||
                (current == Direction.RIGHT && newDirection == Direction.LEFT)

        if (!isOpposite) {
            _gameState.update { it.copy(direction = newDirection) }
        }
    }

    private fun startGameLoop() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            while (_gameState.value.status == GameStatus.IN_PROGRESS) {
                delay(150)
                moveSnake()
            }
        }
    }

    private fun moveSnake() {
        val current = _gameState.value
        val head = current.snakeCoordinates.first()

        val newHead = when (current.direction) {
            Direction.UP -> Pair(head.first, head.second - 1)
            Direction.DOWN -> Pair(head.first, head.second + 1)
            Direction.LEFT -> Pair(head.first - 1, head.second)
            Direction.RIGHT -> Pair(head.first + 1, head.second)
        }

        // Check wall collision
        if (newHead.first !in 0..<gridSize ||
            newHead.second < 0 || newHead.second >= gridSize) {
            gameOver()
            return
        }

        // Check self collision
        if (current.snakeCoordinates.contains(newHead)) {
            gameOver()
            return
        }

        // Check if snake ate food
        val ateFood = newHead == current.foodPosition

        val newSnake = if (ateFood) {
            // Grow snake (don't remove tail)
            listOf(newHead) + current.snakeCoordinates
        } else {
            // Move snake normally
            listOf(newHead) + current.snakeCoordinates.dropLast(1)
        }

        _gameState.update {
            it.copy(
                snakeCoordinates = newSnake,
                score = if (ateFood) it.score + 10 else it.score,
                foodPosition = if (ateFood) spawnFood(newSnake) else it.foodPosition
            )
        }
    }

    private fun spawnFood(snakeCoordinates: List<Pair<Int, Int>>): Pair<Int, Int> {
        var foodPos: Pair<Int, Int>
        do {
            foodPos = Pair(
                (0 until gridSize).random(),
                (0 until gridSize).random()
            )
        } while (snakeCoordinates.contains(foodPos))
        return foodPos
    }

    private fun gameOver() {
        gameJob?.cancel()
        _gameState.update { it.copy(status = GameStatus.GAME_OVER) }

        viewModelScope.launch {
            saveScoreToDatabase(_gameState.value.score)
        }
    }

    private suspend fun saveScoreToDatabase(score: Int) {
        val score1 = Scores(score = score)
        scoresRepository.addScore(score = score1)
    }

    fun getScoreList(): StateFlow<List<Scores>> {
        return scoresRepository.getTopScores()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
    }

    override fun onCleared() {
        super.onCleared()
        gameJob?.cancel()
    }
}
