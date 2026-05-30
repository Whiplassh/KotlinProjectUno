package com.example.projectuno.module_5.task_1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries: StateFlow<List<DiaryEntry>> = _entries.asStateFlow()

    private val filesDir: File = application.filesDir

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch(Dispatchers.IO) {
            val fileList = filesDir.listFiles { _, name -> name.endsWith(".txt") } ?: emptyArray()
            val loadedEntries = fileList.mapNotNull { file ->
                try {
                    val content = file.readText()
                    val fileName = file.name
                    val timestampStr = fileName.substringBefore("_")
                    val timestamp = timestampStr.toLongOrNull() ?: file.lastModified()
                    val title = fileName.substringAfter("_").removeSuffix(".txt")
                    DiaryEntry(fileName, title, content, timestamp)
                } catch (e: Exception) {
                    null
                }
            }.sortedByDescending { it.timestamp }
            _entries.value = loadedEntries
        }
    }

    fun saveEntry(title: String, content: String, existingFileName: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val timestamp = if (existingFileName != null) {
                existingFileName.substringBefore("_").toLongOrNull() ?: System.currentTimeMillis()
            } else {
                System.currentTimeMillis()
            }
            val fileName = "${timestamp}_${title.ifBlank { "NoTitle" }}.txt"
            val file = File(filesDir, fileName)

            if (existingFileName != null && existingFileName != fileName) {
                File(filesDir, existingFileName).delete()
            }

            file.writeText(content)

            val newEntry = DiaryEntry(fileName, title, content, timestamp)
            val currentList = _entries.value.toMutableList()
            
            if (existingFileName != null) {
                val index = currentList.indexOfFirst { it.fileName == existingFileName }
                if (index != -1) {
                    currentList[index] = newEntry
                } else {
                    currentList.add(0, newEntry)
                }
            } else {
                currentList.add(0, newEntry)
            }
            _entries.value = currentList.sortedByDescending { it.timestamp }
        }
    }

    fun deleteEntry(entry: DiaryEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(filesDir, entry.fileName)
            if (file.exists()) {
                file.delete()
            }
            _entries.value = _entries.value.filter { it.fileName != entry.fileName }
        }
    }
}
