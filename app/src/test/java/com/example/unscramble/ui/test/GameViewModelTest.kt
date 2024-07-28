package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNotSame
import junit.framework.Assert.assertTrue
import org.junit.Test

class GameViewModelTest { // too write a model adn test it we need a clas to implemetn the testing.
    private val viewModel = GameViewModel()

    @Test
    fun gameViewMOdel_correctWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        // here the first one   is the thing that we want to test.
        // the second thing that trigger the test and
        // third = end result of the test.

        var currentGameUiState = viewModel.uiState.value

        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState = viewModel.uiState.value

     //   assertFalse(currentGameUiState.isGuessWordWrong)
       assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER , currentGameUiState.score)

    }


    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
        private const val SCORE_AFTER_FIRST_INCORRECT_ANSWER = 0
    }

    @Test
    fun gamviewMOdel_wrongWordGuessed_scoreRemainSameAnderrorFlagSet () {
        val userGuess = "zero"
        viewModel.updateUserGuess(userGuess)
        viewModel.checkUserGuess()
        val currentUiState = viewModel.uiState.value
        assertEquals(SCORE_AFTER_FIRST_INCORRECT_ANSWER , currentUiState.score)
        assertTrue(currentUiState.isGuessWordWrong)
    }




    @Test
    fun gameViewModel_Initialization_FirdtWordLoaded () {
        val gameUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(gameUiState.currentScrambledWord)
        assertFalse(gameUiState.isGameOver)
        assertFalse(gameUiState.isGuessWordWrong)
        assertTrue(gameUiState.currentWordCount == 1)
        assertTrue(gameUiState.score == 0 )
        assertNotSame(gameUiState.currentScrambledWord , unScrambledWord)
    }

    @Test
    fun gameViewModel_finalRound_GameEnd () {
        var  expectedScore = 0
        var currentUiState = viewModel.uiState.value
        var  correctPlayerWord = getUnscrambledWord(currentUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS){
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentUiState.currentScrambledWord)
            assertEquals(expectedScore , currentUiState.score)
        }
        assertTrue(currentUiState.currentWordCount == MAX_NO_OF_WORDS)
        assertTrue(currentUiState.isGameOver)
    }

    @Test
    fun viewModel_WorkSkipped_ScoreUNchangedAndWordCountIncreased () {
        var gameUiState = viewModel.uiState.value
        val rightWord = getUnscrambledWord(gameUiState.currentScrambledWord)
        viewModel.updateUserGuess(rightWord)
        viewModel.checkUserGuess()

        gameUiState = viewModel.uiState.value
        val wordCount = gameUiState.currentWordCount
        viewModel.skipWord()
        viewModel.skipWord()
        gameUiState = viewModel.uiState.value
        assertTrue(gameUiState.score ==20)
        assertEquals(gameUiState.currentWordCount , wordCount + 2 )
    }
}





