package com.example.projectuno.module_3.task_7.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectuno.module_3.task_7.presentation.ui.screen.TodoDetailScreen
import com.example.projectuno.module_3.task_7.presentation.ui.screen.TodoListScreen
import com.example.projectuno.module_3.task_7.presentation.viewmodel.TodoViewModel


object Routes{
    const val LIST = "list"
    const val DETAIL = "detail"
    const val DETAIL_ROUTE = "detail/{id}"
    fun detai(id: Int) = "detail/$id"
}

@Composable
fun AppNavHost(){
    val navController = rememberNavController()
    val todoViewModel: TodoViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ){
        composable(Routes.LIST){
            TodoListScreen(
                viewModel = todoViewModel,
                onOpenDetails = { id ->
                    navController.navigate(Routes.detai(id))
                }
            )
        }
        composable(
            route = Routes.DETAIL_ROUTE,
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            TodoDetailScreen(
                todoId = id,
                viewModel = todoViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}