package com.example.projectuno.module_3.task_7.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectuno.module_3.task_7.data.local.TodoJsonDataSource
import com.example.projectuno.module_3.task_7.data.repository.TodoRepositoryImpl
import com.example.projectuno.module_3.task_7.domain.model.TodoItem
import com.example.projectuno.module_3.task_7.domain.usecase.GetTodosUseCase
import com.example.projectuno.module_3.task_7.domain.usecase.ToggleTodoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TodoViewModel(app: Application) : AndroidViewModel(app){
    private val repository = TodoRepositoryImpl(TodoJsonDataSource(app.applicationContext))
    private val getTodosUseCase = GetTodosUseCase(repository)
    private val toggleTodoUseCase = ToggleTodoUseCase(repository)
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos

    fun loadTodos(){
        viewModelScope.launch {
            _todos.value = getTodosUseCase()
        }

    }
    fun toggleTodo(id: Int) {
        viewModelScope.launch {
            toggleTodoUseCase(id)
            _todos.value = getTodosUseCase()
        }
    }
 fun getTodoById(id: Int): TodoItem? = _todos.value.firstOrNull { it.id == id }

}