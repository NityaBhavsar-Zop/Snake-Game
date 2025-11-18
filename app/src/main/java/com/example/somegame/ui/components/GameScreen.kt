package com.example.somegame.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.somegame.data.model.Direction
import com.example.somegame.data.model.GameState
import com.example.somegame.data.model.GameStatus

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameState: GameState = GameState(),
    onStartGame: () -> Unit = {},
    onRestartGame: () -> Unit = {},
    onDirectionChange: (Direction) -> Unit = {}
) {
    Column(modifier = modifier) {
        when (gameState.status) {
            GameStatus.NOT_STARTED -> {
                Box {
                    GameCanvas(
                        snakeCoordinates = gameState.snakeCoordinates,
                        foodPosition = null,
                        onDirectionChange = onDirectionChange,
                        status = gameState.status
                    )
                    Button(
                        onClick = onStartGame,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 320.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Start Game")
                    }
                }
            }

            GameStatus.IN_PROGRESS -> {
                GameCanvas(
                    snakeCoordinates = gameState.snakeCoordinates,
                    foodPosition = gameState.foodPosition,
                    onDirectionChange = onDirectionChange,
                    status = gameState.status
                )
            }

            GameStatus.GAME_OVER -> {
                Dialog(
                    onDismissRequest = {},
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(modifier = Modifier.wrapContentSize()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Game Over",
                                fontSize = 32.sp,
                                color = Color.Red
                            )
                            Text(
                                text = "Score: ${gameState.score}",
                                fontSize = 24.sp,
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            Button(
                                onClick = onRestartGame,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text("Restart Game")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameCanvas(
    snakeCoordinates: List<Pair<Int, Int>>,
    foodPosition: Pair<Int, Int>?,
    onDirectionChange: (Direction) -> Unit,
    status: GameStatus
) {
    var dragStart by rememberSaveable { mutableStateOf<Offset?>(null) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .pointerInput(status) {
                if (status == GameStatus.IN_PROGRESS) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStart = offset
                        },
                        onDragEnd = {
                            dragStart = null
                        },
                        onDrag = { change, dragAmount ->
                            dragStart?.let { start ->
                                val end = change.position
                                val deltaX = end.x - start.x
                                val deltaY = end.y - start.y

                                // Detect direction based on larger delta
                                if (kotlin.math.abs(deltaX) > kotlin.math.abs(deltaY)) {
                                    if (kotlin.math.abs(deltaX) > 50) { // Minimum swipe distance
                                        onDirectionChange(
                                            if (deltaX > 0) Direction.RIGHT else Direction.LEFT
                                        )
                                        dragStart = null // Reset to prevent multiple triggers
                                    }
                                } else {
                                    if (kotlin.math.abs(deltaY) > 50) {
                                        onDirectionChange(
                                            if (deltaY > 0) Direction.DOWN else Direction.UP
                                        )
                                        dragStart = null
                                    }
                                }
                            }
                        }
                    )
                }
            }
    ) {
        val cellSize = size.width / 21

        // Draw grid
        for (i in 0..20) {
            drawLine(
                Color.DarkGray,
                Offset(i * cellSize, 0f),
                Offset(i * cellSize, size.height)
            )
            drawLine(Color.DarkGray, Offset(0f, i * cellSize), Offset(size.width, i * cellSize))
        }

        // Draw food
        foodPosition?.let { food ->
            drawRect(
                color = Color.Red,
                topLeft = Offset(food.first * cellSize, food.second * cellSize),
                size = Size(cellSize, cellSize)
            )
        }

        // Draw snake
        snakeCoordinates.forEachIndexed { index, segment ->
            drawRect(
                color = if (index == 0) Color.Yellow else Color.Green, // Head is different color
                topLeft = Offset(segment.first * cellSize, segment.second * cellSize),
                size = Size(cellSize, cellSize)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen(
        modifier = Modifier.size(400.dp),
        gameState = GameState(status = GameStatus.NOT_STARTED)
    )
}