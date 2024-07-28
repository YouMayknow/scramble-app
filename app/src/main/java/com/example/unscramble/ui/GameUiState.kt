package com.example.unscramble.ui

 data class GameUiState(
    val currentScrambledWord : String = "" ,
     val isGuessWordWrong : Boolean = false ,
     val score : Int = 0 ,
     val currentWordCount : Int  = 1 ,
     val  isGameOver : Boolean = false

)
