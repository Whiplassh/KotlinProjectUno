package com.example.projectuno.module_6.task_3.presentation.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectuno.module_6.task_3.data.local.TokenManager
import com.example.projectuno.module_6.task_3.data.repository.AuthRepositoryImpl
import com.example.projectuno.module_6.task_3.presentation.viewmodel.AuthViewModel
import com.example.projectuno.module_6.task_3.presentation.viewmodel.AuthViewModelFactory

@Composable
fun AuthApp() {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val repository = remember { AuthRepositoryImpl(tokenManager) }
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository))
    
    val navController = rememberNavController()
    val token by viewModel.token.collectAsState(initial = null)

    LaunchedEffect(token) {
        if (token == null) {
            navController.navigate("login") {
                popUpTo(0)
            }
        } else if (navController.currentDestination?.route == "login") {
            navController.navigate("users") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = if (token == null) "login" else "users") {
        composable("login") {
            LoginScreen(viewModel)
        }
        composable("users") {
            UsersListScreen(viewModel) { userId ->
                navController.navigate("detail/$userId")
            }
        }
        composable("detail/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
            UserDetailScreen(viewModel, userId) {
                navController.popBackStack()
            }
        }
    }
}
