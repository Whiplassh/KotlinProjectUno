package com.example.projectuno.module_6.task_3.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectuno.module_6.task_3.presentation.viewmodel.AuthViewModel
import com.example.projectuno.module_6.task_3.presentation.viewmodel.DetailState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(viewModel: AuthViewModel, userId: Int, onBack: () -> Unit) {
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUserDetail(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Детали пользователя")
                        },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (detailState) {
            is DetailState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is DetailState.Success -> {
                val user = (detailState as DetailState.Success).user
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = user.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier
                        .height(24.dp))
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "@${user.username}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    
                    Button(
                        onClick = { viewModel.logout() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Выйти из аккаунта")
                    }
                }
            }
            is DetailState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (detailState as DetailState.Error).message
                    )
                    Button(
                        onClick = { viewModel.loadUserDetail(userId) }
                    ) {
                        Text("Повторить")
                    }
                }
            }
        }
    }
}
