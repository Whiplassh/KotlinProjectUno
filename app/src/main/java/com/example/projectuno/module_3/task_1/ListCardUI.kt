package com.example.projectuno.module_3.task_1

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

val itemList = listOf(
    ListItemsData(1, "Title 1 ", "Descr 1", R.drawable.ic_menu_camera),
    ListItemsData(2, "Title 2 ", "Descr 2", R.drawable.ic_menu_edit),
    ListItemsData(3, "Title 3 ", "Descr 3", R.drawable.ic_menu_rotate),
    ListItemsData(4, "Title 4 ", "Descr 4", R.drawable.ic_menu_help),
    ListItemsData(5, "Title 5 ", "Descr 5", R.drawable.ic_menu_delete)
)
@Composable
fun ItemCard(item: ListItemsData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(64.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.idPic),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ListScroll(items:List<ListItemsData>){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items){
                item -> ItemCard(item = item)
        }
    }
}
