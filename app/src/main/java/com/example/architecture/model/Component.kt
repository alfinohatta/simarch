package com.example.architecture.model

import com.example.architecture.core.model.ComponentCategory
import com.example.architecture.core.model.ComponentData
import com.example.architecture.data.repository.ComponentRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object SystemComponents {
    private val repo = ComponentRepositoryImpl()

    fun getById(id: String): ComponentData? {
        return runBlocking {
            repo.getComponentById(id).first()
        }
    }

    val allComponents: List<ComponentData>
        get() = runBlocking {
            repo.getAllComponents().first()
        }
}
