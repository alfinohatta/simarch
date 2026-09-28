package com.example.architecture.viewmodel

import androidx.lifecycle.ViewModel
import com.example.architecture.core.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserPreferencesViewModel : ViewModel() {
    private val currentDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val _userProfile = MutableStateFlow(
        UserProfile(
            studentName = "",
            group = "",
            submissionDate = currentDateStr,
            lastScorePercent = 0
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _studentName = MutableStateFlow("")
    val studentName: StateFlow<String> = _studentName.asStateFlow()

    private val _group = MutableStateFlow("")
    val group: StateFlow<String> = _group.asStateFlow()

    private val _submissionDate = MutableStateFlow(currentDateStr)
    val submissionDate: StateFlow<String> = _submissionDate.asStateFlow()

    fun setStudentName(name: String) {
        val sanitized = sanitizeInput(name, maxLength = 60)
        _studentName.value = sanitized
        _userProfile.value = _userProfile.value.copy(studentName = sanitized)
    }

    fun setGroup(grp: String) {
        val sanitized = sanitizeInput(grp, maxLength = 30)
        _group.value = sanitized
        _userProfile.value = _userProfile.value.copy(group = sanitized)
    }

    fun setSubmissionDate(date: String) {
        val sanitized = sanitizeInput(date, maxLength = 20)
        _submissionDate.value = sanitized
        _userProfile.value = _userProfile.value.copy(submissionDate = sanitized)
    }

    private fun sanitizeInput(input: String, maxLength: Int): String {
        return input.take(maxLength)
            .replace("<", "")
            .replace(">", "")
            .replace("\"", "")
            .replace("'", "")
    }
}
