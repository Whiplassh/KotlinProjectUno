package com.example.projectuno.module_4.task_3

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*

data class GitHubRepo(
    val id: Int,
    val full_name: String,
    val description: String?,
    val stargazers_count: Int,
    val language: String?
)

class GitHubRepoDataSource(private val context: Context) {
    private val gson = Gson()

    fun getRepos(): List<GitHubRepo> {
        val json = context.assets.open("github_repos.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<GitHubRepo>>() {}.type
        return gson.fromJson(json, type)
    }
}

fun <T> CoroutineScope.debounce(
    waitMs: Long = 500L,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubSearchScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<GitHubRepo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var searchJob by remember { mutableStateOf<Job?>(null) }

    val allRepos = remember {
        try {
            GitHubRepoDataSource(context).getRepos()
        } catch (e: Exception) {
            emptyList()
        }
    }

    val debouncedSearch = remember {
        scope.debounce<String>(waitMs = 500L) { query ->
            searchJob?.cancel()
            searchJob = scope.launch {
                isLoading = true
                try {
                    delay(800)

                    val filtered = if (query.isBlank()) {
                        emptyList()
                    } else {
                        allRepos.filter { repo ->
                            repo.full_name.contains(query, ignoreCase = true) ||
                            repo.description?.contains(query, ignoreCase = true) == true ||
                            repo.language?.contains(query, ignoreCase = true) == true
                        }
                    }

                    searchResults = filtered
                } catch (e: CancellationException) {
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
            .padding(16.dp)
    ) {
        Text(
            text = "GitHub Repository Search",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
                debouncedSearch(newQuery)
            },
            label = { Text("Search repositories") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier
                .height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResults) { repo ->
                RepoCard(repo)
            }

            if (searchResults.isEmpty() && searchQuery.isNotBlank() && !isLoading) {
                item {
                    Text(
                        text = "No repositories found",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RepoCard(repo: GitHubRepo) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = repo.full_name,
                style = MaterialTheme.typography.titleMedium
            )

            if (repo.description != null) {
                Spacer(modifier = Modifier
                    .height(4.dp))
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier
                .height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (repo.language != null) {
                    Text(
                        text = "Language: ${repo.language}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "${repo.stargazers_count}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
