package com.example.projectuno.module_3.task_7.data.model

import com.example.projectuno.module_3.task_7.domain.model.TodoItem

fun TodoItemDto.toDomain(): TodoItem = TodoItem(
    id = id,
    title = title,
    description= description,
    isCompleted = isCompleted
)