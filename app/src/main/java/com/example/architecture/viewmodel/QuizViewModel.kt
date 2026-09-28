package com.example.architecture.viewmodel

import androidx.lifecycle.ViewModel
import com.example.architecture.core.model.DefenseQuestion
import com.example.architecture.model.DefenseQuestionBank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QuizViewModel : ViewModel() {
    private val _questions = MutableStateFlow(DefenseQuestionBank.questions)
    val questions: StateFlow<List<DefenseQuestion>> = _questions

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _isFlipped = MutableStateFlow(false)
    val isFlipped: StateFlow<Boolean> = _isFlipped

    private val _isQuizMode = MutableStateFlow(false)
    val isQuizMode: StateFlow<Boolean> = _isQuizMode

    private val _selectedOption = MutableStateFlow<Int?>(null)
    val selectedOption: StateFlow<Int?> = _selectedOption

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    fun toggleMode() {
        _isQuizMode.value = !_isQuizMode.value
        resetQuestionState()
    }

    fun flipCard() {
        _isFlipped.value = !_isFlipped.value
    }

    fun nextQuestion() {
        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value++
            resetQuestionState()
        }
    }

    fun previousQuestion() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
            resetQuestionState()
        }
    }

    fun shuffleQuestions() {
        _questions.value = _questions.value.shuffled()
        _currentIndex.value = 0
        _score.value = 0
        resetQuestionState()
    }

    fun selectOption(index: Int) {
        if (_selectedOption.value == null) {
            _selectedOption.value = index
            if (index == _questions.value[_currentIndex.value].correctIndex) {
                _score.value++
            }
        }
    }

    private fun resetQuestionState() {
        _isFlipped.value = false
        _selectedOption.value = null
    }
}
