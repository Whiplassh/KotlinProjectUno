package com.example.projectuno.module_3.task_7.domain.repository

import com.example.projectuno.module_3.task_7.domain.model.TodoItem

interface TodoRepository{
    suspend fun getTodos(): List<TodoItem>
    suspend fun toggleTodo(id: Int)
}