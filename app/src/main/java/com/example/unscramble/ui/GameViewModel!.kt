package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// here the below class  is as view model
class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())  // here uiState is collecting refernce data form the
    // class GamuiState within the mutabelStabelFlow that enable data to be convert itno state and
    // ensure the update will be kept under the eye of ui elemtns .
val uiState : StateFlow<GameUiState> = _uiState.asStateFlow()    // doing this so the state cannot be changed outside by any #encapsualtion
    // other external things and we will direct this as an output of above code.
   private lateinit var currentWord : String // laterinit helps to declare properties without initializing them immediately #immutability
   private var usedWords : MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")

    private fun shuffleCurremtWord(word : String) : String {
        val temWord = word.toCharArray()
        temWord.shuffle()
        while (String(temWord).equals(word)) {
            temWord.shuffle()
        }
        return String(temWord)
    }

    fun resetGame () {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWorkAndShuffle()) // it is a property
        // of stateflow us to update  the value of the mutable State Flow.
    }
   private  fun pickRandomWorkAndShuffle() : String {
        currentWord = allWords.random()
        if(usedWords.contains(currentWord)){
            return pickRandomWorkAndShuffle ()
        }  else {
            usedWords.add(currentWord)
            return shuffleCurremtWord(currentWord)
        }
    }

    init { // it initialize the object right after it's creation menas it iwlll initlialize the whole class as soon it is created
        // and along with it run the code witih it's body.

        resetGame()
    }

    fun updateUserGuess (guessedWord : String) {
        userGuess = guessedWord
    }

    fun checkUserGuess () {
        if(userGuess.equals(currentWord , ignoreCase = true)) {
            val updateScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updateScore)

        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessWordWrong = true)  // we are passing the new parmater to the data class bcz
                // this enables us to chreate a new instance of the state with each new change in the paramreter ,
            }
        }
        updateUserGuess("")
    }

    fun updateGameState( updatedSCore:Int )  {
        if(usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessWordWrong = false ,
                    score = updatedSCore ,
                    isGameOver = true
                )
            }
        }else{
            _uiState.update {CurrentState ->
                CurrentState.copy(
                    isGuessWordWrong = false ,
                    currentScrambledWord = pickRandomWorkAndShuffle() ,
                    score = updatedSCore ,
                    currentWordCount = CurrentState.currentWordCount.inc()  // it return the value incremented by one
                )
            }
        }

    }
    fun skipWord () {
        updateGameState(_uiState.value.score)
        updateUserGuess("")

    }


}

