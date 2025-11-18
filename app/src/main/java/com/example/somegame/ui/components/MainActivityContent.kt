package com.example.somegame.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.somegame.data.model.Direction
import com.example.somegame.data.model.GameState
import com.example.somegame.data.model.Scores

@Composable
fun MainActivityContent(
    modifier: Modifier = Modifier,
    gameState: GameState = GameState(),
    scoresList: List<Scores> = listOf(),
    onStartGame: () -> Unit = { },
    onRestartGame: () -> Unit = { },
    onDirectionChange: (Direction) -> Unit = { },
) {

    Column(
        modifier = modifier
    ) {
        GameScreen(
            modifier = modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally),
            gameState = gameState,
            onStartGame = onStartGame,
            onRestartGame = onRestartGame,
            onDirectionChange = onDirectionChange
        )

        LazyColumn(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(
                scoresList,
            ) {  index, item  ->
                ScoreBoard(
                    rank = (index + 1).toString(),
                    score = item.score.toString()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    MainActivityContent()
}