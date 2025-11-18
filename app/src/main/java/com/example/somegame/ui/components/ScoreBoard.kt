package com.example.somegame.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScoreBoard(
    rank: String = "",
    score: String = "",
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(4.dp, color = Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = rank
            )

            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Score: ")
                Text(text = score)
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScoreBoardPreview() {
    ScoreBoard(rank = "1", score = "23")
}