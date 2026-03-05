package com.example.projectuno.module_3.task_7.data.repository

import com.example.projectuno.module_3.task_7.data.local.TodoJsonDataSource
import com.example.projectuno.module_3.task_7.data.model.toDomain
import com.example.projectuno.module_3.task_7.domain.model.TodoItem
import com.example.projectuno.module_3.task_7.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoRepositoryImpl(
    private val dataSource: TodoJsonDataSource
) : TodoRepository {
    private var cached: MutableList<TodoItem>? = null

    override suspend fun getTodos(): List<TodoItem> = withContext(Dispatchers.IO) {
        val current = cached
        if (current != null) return@withContext current.toList()

        val loaded = dataSource.getTodos()
            .map {it.toDomain()}
            .toMutableList()
        cached = loaded
        loaded.toList()
    }

    override suspend fun toggleTodo(id: Int) = withContext(Dispatchers.Default) {
        val list = cached ?: return@withContext
        val index = list.indexOfFirst { it.id == id }
        if (index == -1) return@withContext

        val old = list[index]
        list[index] = old.copy(isCompleted = !old.isCompleted)
    }
}
