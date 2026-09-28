package com.example.architecture.model

import com.example.architecture.core.model.DefenseQuestion
import com.example.architecture.data.repository.QuestionRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object DefenseQuestionBank {
    private val repo = QuestionRepositoryImpl()

    val questions: List<DefenseQuestion>
        get() = runBlocking {
            repo.getQuestions().first()
        }
}
