package com.example.projectuno.module_6.task_3.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectuno.module_6.task_3.domain.model.User
import com.example.projectuno.module_6.task_3.presentation.viewmodel.AuthViewModel
import com.example.projectuno.module_6.task_3.presentation.viewmodel.UsersState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(viewModel: AuthViewModel, onUserClick: (Int) -> Unit) {
    val usersState by viewModel.usersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Пользователи")
                        },
                actions = {
                    IconButton(
                        onClick = { viewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (usersState) {
            is UsersState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UsersState.Success -> {
                val users = (usersState as UsersState.Success).users
                LazyColumn(
                    modifier = Modifier
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users) { user ->
                        UserItem(user = user, onClick = { onUserClick(user.id) })
                    }
                }
            }
            is UsersState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (usersState as UsersState.Error).message
                    )
                    Button(
                        onClick = { viewModel.loadUsers() }
                    ) {
                        Text("Повторить")
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.image,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )
            Column {
                Text(text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "@${user.username}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(text = user.email,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
