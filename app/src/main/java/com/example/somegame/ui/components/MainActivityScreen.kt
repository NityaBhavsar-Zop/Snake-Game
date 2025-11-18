package com.example.somegame.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.somegame.presentation.MainViewModel

@Composable
fun MainActivityScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val gameState = viewModel.gameState.collectAsState()
    val scoresList = viewModel.getScoreList().collectAsState()
    MainActivityContent(
        modifier = modifier,
        gameState = gameState.value,
        scoresList = scoresList.value,
        onStartGame = { viewModel.startGame() },
        onRestartGame = { viewModel.restartGame() },
        onDirectionChange = { direction -> viewModel.changeDirection(direction) }
    )
}