package com.example.somegame.data.model

enum class GameStatus {
    NOT_STARTED,
    IN_PROGRESS,
    GAME_OVER
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class GameState(
    val status: GameStatus = GameStatus.NOT_STARTED,
    val snakeCoordinates: List<Pair<Int, Int>> = listOf(
        Pair(10, 10),
        Pair(10, 11),
        Pair(10, 12),
        Pair(10, 13),
        Pair(10, 14)
    ),
    val direction: Direction = Direction.UP,
    val score: Int = 0,
    val foodPosition: Pair<Int, Int> = Pair(0, 0)
)
