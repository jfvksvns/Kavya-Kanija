package com.kavyakanaja.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kavyakanaja.app.data.api.RetrofitClient
import com.kavyakanaja.app.data.repository.ExplainRepository
import com.kavyakanaja.app.data.repository.QuizResult
import com.kavyakanaja.app.model.QuizResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class QuizUiState {
    object Idle    : QuizUiState()
    object Loading : QuizUiState()
    data class Success(val data: QuizResponse) : QuizUiState()
    data class Error(val message: String) : QuizUiState()
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ExplainRepository(RetrofitClient.apiService)

    private val _state = MutableStateFlow<QuizUiState>(QuizUiState.Idle)
    val state: StateFlow<QuizUiState> = _state.asStateFlow()

    // Quiz progress
    private val _currentMcqIndex = MutableStateFlow(0)
    val currentMcqIndex: StateFlow<Int> = _currentMcqIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val selectedAnswers: StateFlow<Map<Int, String>> = _selectedAnswers.asStateFlow()

    private val _quizFinished = MutableStateFlow(false)
    val quizFinished: StateFlow<Boolean> = _quizFinished.asStateFlow()

    fun loadQuiz(
        poemText: String,
        language: String,
        numQuestions: Int = 5,
        difficulty: String = "medium"
    ) {
        _state.value = QuizUiState.Loading
        _currentMcqIndex.value = 0
        _score.value = 0
        _selectedAnswers.value = emptyMap()
        _quizFinished.value = false

        viewModelScope.launch {
            _state.value = when (val r = repository.generateQuiz(
                poemText, language, numQuestions, difficulty
            )) {
                is QuizResult.Success -> QuizUiState.Success(r.data)
                is QuizResult.Error   -> QuizUiState.Error(r.message)
            }
        }
    }

    fun selectAnswer(questionIndex: Int, answer: String) {
        val current = _selectedAnswers.value.toMutableMap()
        current[questionIndex] = answer
        _selectedAnswers.value = current
    }

    fun nextQuestion(totalQuestions: Int) {
        if (_currentMcqIndex.value < totalQuestions - 1) {
            _currentMcqIndex.value++
        } else {
            _quizFinished.value = true
            calculateScore()
        }
    }

    private fun calculateScore() {
        val quizData = (_state.value as? QuizUiState.Success)?.data ?: return
        val questions = quizData.mcqQuestions ?: return
        var correct = 0
        questions.forEachIndexed { index, question ->
            if (_selectedAnswers.value[index] == question.correctAnswer) correct++
        }
        _score.value = correct
    }

    fun resetQuiz() {
        _state.value = QuizUiState.Idle
        _currentMcqIndex.value = 0
        _score.value = 0
        _selectedAnswers.value = emptyMap()
        _quizFinished.value = false
    }
}