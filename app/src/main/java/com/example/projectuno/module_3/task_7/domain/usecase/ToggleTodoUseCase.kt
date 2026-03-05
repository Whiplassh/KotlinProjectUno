package com.example.projectuno.module_3.task_7.domain.usecase

import com.example.projectuno.module_3.task_7.domain.repository.TodoRepository

class ToggleTodoUseCase(private val repository: TodoRepository){
    suspend operator fun invoke(id: Int) = repository.toggleTodo(id)

}