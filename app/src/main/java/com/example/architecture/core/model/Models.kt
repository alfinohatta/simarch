package com.example.architecture.core.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class Characteristic(
    val label: String,
    val value: String
)

@Immutable
data class StorageComparisonRow(
    val type: String,
    val capacity: String,
    val interfaceType: String,
    val readSpeed: String
)

enum class ComponentCategory(val displayName: String) {
    ALL("All Components"),
    CORE("Core Processors"),
    MEMORY("Memory & Storage"),
    FIRMWARE_OS("Firmware & OS"),
    PERIPHERALS("I/O & Network")
}

@Immutable
data class ComponentData(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val category: ComponentCategory,
    val mainFunction: String,
    val architectureRole: String,
    val simpleAnalogy: String = "",
    val characteristics: List<Characteristic>,
    val isStorageDevice: Boolean = false,
    val storageComparison: List<StorageComparisonRow>? = null,
    val storageExplanation: String? = null
)

@Immutable
data class DefenseQuestion(
    val id: Int,
    val category: String,
    val question: String,
    val answer: String,
    val options: List<String>,
    val correctIndex: Int
)

@Immutable
data class UserProfile(
    val studentName: String = "",
    val group: String = "",
    val submissionDate: String = "",
    val lastScorePercent: Int = 0
)
