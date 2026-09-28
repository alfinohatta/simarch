package com.example.architecture.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.architecture.data.repository.ComponentRepositoryImpl
import com.example.architecture.data.repository.QuestionRepositoryImpl
import com.example.architecture.data.repository.UserPreferencesRepositoryImpl

class AppViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    private val componentRepository by lazy { ComponentRepositoryImpl() }
    private val questionRepository by lazy { QuestionRepositoryImpl() }
    private val userPreferencesRepository by lazy { UserPreferencesRepositoryImpl(context.applicationContext) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserPreferencesViewModel::class.java) -> {
                UserPreferencesViewModel() as T
            }
            modelClass.isAssignableFrom(StartupViewModel::class.java) -> {
                StartupViewModel() as T
            }
            modelClass.isAssignableFrom(ExecutionViewModel::class.java) -> {
                ExecutionViewModel() as T
            }
            modelClass.isAssignableFrom(QuizViewModel::class.java) -> {
                QuizViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
