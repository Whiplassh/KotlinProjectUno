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
import com.example.projectuno.module_4.task_3.GitHubSearchScreen
import com.example.projectuno.module_4.task_4.SocialFeedScreen
import com.example.projectuno.module_4.task_7.MusicPlayerScreen
import com.example.projectuno.module_4.task_10.LocationScreen
import com.example.projectuno.module_4.task_13.FlowDemoScreen
import com.example.projectuno.module_4.task_14.SensorScreen
import com.example.projectuno.module_5.task_1.DiaryScreen
import com.example.projectuno.module_5.task_2.PhotoGalleryScreen
import com.example.projectuno.module_6.task_3.presentation.ui.AuthApp
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

//                AppNavHost()

//                4.3 Экран поиска GitHub репозиториев с debounce
//                GitHubSearchScreen()

//                4.4 Социальная лента
//                SocialFeedScreen()

//                4.7 Музыкальный плеер
//                MusicPlayerScreen()

//                4.10 Определение местоположения
//                LocationScreen()

//                4.13 Flow Demo
//                FlowDemoScreen()

//                4.14 Сенсоры
//                SensorScreen()

//                5.1 Дневник
//                DiaryScreen()

//                5.2 Фотогалерея
//                PhotoGalleryScreen()

//                6.3 Авторизация
                AuthApp()
                }
            }
        }
    }


