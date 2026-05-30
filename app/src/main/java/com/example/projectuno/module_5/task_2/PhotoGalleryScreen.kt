package com.example.projectuno.module_5.task_2

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(viewModel: PhotoViewModel = viewModel()) {
    val context = LocalContext.current
    val photos by viewModel.photos.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.loadPhotos()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true) {
            val file = viewModel.getNewPhotoFile()
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Разрешение на камеру отклонено", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Фотогалерея") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Take Photo")
            }
        }
    ) { padding ->
        if (photos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment
                        .CenterHorizontally
                ) {
                    Text(
                        "У вас пока нет фото",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )
                    Button(
                        onClick = {permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA)) }
                    ) {
                        Text(
                            "Сделать первое фото"
                        )
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(photos) { file ->
                    PhotoItem(
                        file = file,
                        onExport = {
                            viewModel.exportToGallery(file) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Фото добавлено в галерею")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoItem(file: File, onExport: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .aspectRatio(1f)
        .clickable { showDialog = true }
    ) {
        AsyncImage(
            model = file,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Опции фото") },
            text = { Text("Экспортировать это фото в общую галерею?") },
            confirmButton = {
                TextButton(onClick = {
                    onExport()
                    showDialog = false
                }) {
                    Text("Экспорт")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}
