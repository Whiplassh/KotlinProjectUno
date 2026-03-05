package com.example.projectuno.module_3.task_7.domain.usecase

import com.example.projectuno.module_3.task_7.domain.model.TodoItem
import com.example.projectuno.module_3.task_7.domain.repository.TodoRepository

class GetTodosUseCase(private val repository: TodoRepository){
    suspend operator fun invoke(): List<TodoItem> = repository.getTodos()
}