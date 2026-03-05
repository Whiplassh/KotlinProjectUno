package com.example.projectuno.module_3.task_7.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectuno.module_3.task_7.presentation.viewmodel.TodoViewModel

@Composable
fun TodoDetailScreen(
    todoId: Int,
    viewModel: TodoViewModel,
    onBack: () -> Unit
) {
    val item = viewModel.getTodoById(todoId)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (item == null) {
            Text(
                text = "NO task found",
                style = MaterialTheme.typography.titleMedium)
    } else {

        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge
        )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Text(
                text = if (item.isCompleted) "Completed" else "Not completed",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(
            modifier = Modifier
                .height(24.dp)
        )
        Button(onClick = onBack) {
            Text(text = "Back")
        }
    }
}