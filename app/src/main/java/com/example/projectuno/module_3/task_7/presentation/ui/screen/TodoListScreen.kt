package com.example.projectuno.module_3.task_7.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.projectuno.module_3.task_7.presentation.ui.component.TodoItemCard
import com.example.projectuno.module_3.task_7.presentation.viewmodel.TodoViewModel

@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    onOpenDetails: (Int) -> Unit
){
    val todos by viewModel.todos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTodos()
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ){
        items(todos, key = {it.id }){
            item -> TodoItemCard(
                item = item,
                onClick = {onOpenDetails(item.id)},
            onToggle = {onOpenDetails(item.id)}
            )
        }
    }
}