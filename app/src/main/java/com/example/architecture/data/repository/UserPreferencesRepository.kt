package com.example.architecture.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.architecture.core.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface UserPreferencesRepository {
    fun getUserProfile(): Flow<UserProfile>
    fun saveUserProfile(profile: UserProfile)
    fun saveQuizScore(score: Int, total: Int)
}

class UserPreferencesRepositoryImpl(context: Context) : UserPreferencesRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("app_user_prefs", Context.MODE_PRIVATE)

    private val _userProfileState = MutableStateFlow(loadProfileFromPrefs())

    override fun getUserProfile(): Flow<UserProfile> = _userProfileState.asStateFlow()

    override fun saveUserProfile(profile: UserProfile) {
        val sanitizedName = profile.studentName.take(60).replace("<", "").replace(">", "")
        val sanitizedGroup = profile.group.take(30).replace("<", "").replace(">", "")
        val sanitizedDate = profile.submissionDate.take(20).replace("<", "").replace(">", "")

        prefs.edit()
            .putString("student_name", sanitizedName)
            .putString("student_group", sanitizedGroup)
            .putString("submission_date", sanitizedDate)
            .apply()

        val updated = profile.copy(
            studentName = sanitizedName,
            group = sanitizedGroup,
            submissionDate = sanitizedDate
        )
        _userProfileState.value = updated
    }

    override fun saveQuizScore(score: Int, total: Int) {
        val percent = if (total > 0) (score * 100) / total else 0
        val currentBest = prefs.getInt("high_score_percent", 0)
        if (percent > currentBest) {
            prefs.edit().putInt("high_score_percent", percent).apply()
        }
        val current = _userProfileState.value
        _userProfileState.value = current.copy(lastScorePercent = maxOf(percent, currentBest))
    }

    private fun loadProfileFromPrefs(): UserProfile {
        return UserProfile(
            studentName = prefs.getString("student_name", "") ?: "",
            group = prefs.getString("student_group", "") ?: "",
            submissionDate = prefs.getString("submission_date", "") ?: "",
            lastScorePercent = prefs.getInt("high_score_percent", 0)
        )
    }
}
