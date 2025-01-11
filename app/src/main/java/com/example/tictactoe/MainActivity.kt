package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToeGame()
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    val board = remember { mutableStateListOf(mutableStateListOf("", "", ""), mutableStateListOf("", "", ""), mutableStateListOf("", "", "")) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var draw by remember { mutableStateOf(false) }
    var xWins by remember { mutableStateOf(0) }
    var oWins by remember { mutableStateOf(0) }
    var showCredits by remember { mutableStateOf(false) }

    if (showCredits) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Created by Shubham Ojha",
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showCredits = false }) {
                    Text(text = "Back")
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray) // Set dark mode background
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-160).dp), // Moves the button higher by 16dp without affecting other elements
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { showCredits = true },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Credits")
                }
            }

            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "X Wins: $xWins",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = "O Wins: $oWins",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            if (winner != null) {
                Text(
                    text = "$winner wins!",
                    fontSize = 24.sp,
                    color = Color.Green,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (draw) {
                Text(
                    text = "It's a draw!",
                    fontSize = 24.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            for (row in 0..2) {
                Row {
                    for (col in 0..2) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                                .background(Color.Gray, RoundedCornerShape(8.dp))
                                .clickable(enabled = winner == null && !draw && board[row][col].isEmpty()) {
                                    board[row][col] = currentPlayer
                                    if (checkWinner(board, currentPlayer)) {
                                        winner = currentPlayer
                                        if (currentPlayer == "X") {
                                            xWins++
                                        } else {
                                            oWins++
                                        }
                                    } else if (isDraw(board)) {
                                        draw = true
                                    } else {
                                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = board[row][col],
                                fontSize = 32.sp,
                                color = Color.White, // Text color for dark mode
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    for (r in 0..2) {
                        for (c in 0..2) {
                            board[r][c] = ""
                        }
                    }
                    currentPlayer = "X"
                    winner = null
                    draw = false
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Reset Game")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTicTacToeGame() {
    TicTacToeTheme {
        TicTacToeGame()
    }
}

fun checkWinner(board: List<List<String>>, player: String): Boolean {
    for (i in 0..2) {
        if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true
        if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true
    }

    if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true
    if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true

    return false
}

fun isDraw(board: List<List<String>>): Boolean {
    return board.all { row -> row.all { cell -> cell.isNotEmpty() } }
}