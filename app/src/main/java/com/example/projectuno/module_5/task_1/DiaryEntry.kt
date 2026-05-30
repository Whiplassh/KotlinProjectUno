package com.example.projectuno.module_5.task_1

data class DiaryEntry(
    val fileName: String,
    val title: String,
    val content: String,
    val timestamp: Long
) {
    val displayContent: String
        get() = if (content.length > 40) content.take(40) + "..." else content
}
