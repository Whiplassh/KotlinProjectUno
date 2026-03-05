package com.example.projectuno


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.projectuno.module_2.MishinGlass5
import com.example.projectuno.module_3.task_1.ListScroll
import com.example.projectuno.module_3.task_1.itemList
import com.example.projectuno.module_3.task_4.SaveDataScreen
import com.example.projectuno.module_3.task_7.navigation.AppNavHost
import com.example.projectuno.ui.theme.ProjectUnoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectUnoTheme {
//                MishinGlass5()

//                val items = itemList
//                ListScroll(items = items)

//                SaveDataScreen()

                AppNavHost()
                }
            }
        }
    }


