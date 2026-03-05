package com.example.projectuno.module_3.task_7.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectuno.module_3.task_7.domain.model.TodoItem



@Composable
fun TodoItemCard(
    item: TodoItem,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {onClick()}
    ){
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier.weight(1f)
            ){
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium)

                Spacer(
                    modifier = Modifier
                        .height(4.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = onToggle
            )

        }
    }
}