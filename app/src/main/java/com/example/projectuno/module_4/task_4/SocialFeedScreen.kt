package com.example.projectuno.module_4.task_4

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*

data class SocialPost(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val avatarUrl: String
)

data class Comment(
    val postId: Int,
    val id: Int,
    val name: String,
    val body: String
)

class SocialDataSource(private val context: Context) {
    private val gson = Gson()

    fun getPosts(): List<SocialPost> {
        val json = context.assets.open("social_posts.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<SocialPost>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getComments(): List<Comment> {
        val json = context.assets.open("comments.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Comment>>() {}.type
        return gson.fromJson(json, type)
    }
}

sealed class PostLoadState {
    object Loading : PostLoadState()
    data class Ready(
        val post: SocialPost,
        val avatarColor: String,
        val comments: List<Comment>
    ) : PostLoadState()
    data class Error(val message: String) : PostLoadState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialFeedScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val postStates = remember { mutableStateMapOf<Int, PostLoadState>() }
    var loadingJob by remember { mutableStateOf<Job?>(null) }

    val dataSource = remember { SocialDataSource(context) }

    val allPosts = remember {
        try {
            dataSource.getPosts()
        } catch (e: Exception) {
            emptyList()
        }
    }

    val allComments = remember {
        try {
            dataSource.getComments()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun loadPosts() {
        loadingJob?.cancel()

        postStates.clear()
        allPosts.forEach { post ->
            postStates[post.id] = PostLoadState.Loading
        }

        loadingJob = scope.launch {
            supervisorScope {
                allPosts.forEach { post ->
                    launch {
                        try {
                            val avatarColor = withContext(Dispatchers.IO) {
                                try {
                                    delay((800..1500).random().toLong())

                                    val shouldFail = (0..10).random() == 0
                                    if (shouldFail) throw Exception("Avatar load failed")

                                    val colors = listOf("#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA07A", "#98D8C8", "#F7DC6F", "#BB8FCE", "#85C1E2")
                                    colors.random()
                                } catch (e: Exception) {
                                    "#CCCCCC"
                                }
                            }

                            val comments = withContext(Dispatchers.IO) {
                                try {
                                    delay((1000..2000).random().toLong())

                                    val shouldFail = (0..10).random() == 0
                                    if (shouldFail) throw Exception("Comments load failed")

                                    allComments.filter { it.postId == post.id }
                                } catch (e: Exception) {
                                    emptyList<Comment>()
                                }
                            }

                            postStates[post.id] = PostLoadState.Ready(post, avatarColor, comments)
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            postStates[post.id] = PostLoadState.Error(e.message ?: "Unknown error")
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        loadPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Social Feed",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(onClick = { loadPosts() }) {
                Text("Обновить")
            }
        }

        Spacer(modifier = Modifier
            .height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(allPosts) { post ->
                val state = postStates[post.id] ?: PostLoadState.Loading
                PostCard(state)
            }
        }
    }
}

@Composable
fun PostCard(state: PostLoadState) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        when (state) {
            is PostLoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is PostLoadState.Ready -> {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(state.avatarColor))),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = state.post.avatarUrl,
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)

                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .width(12.dp)
                        )

                        Text(
                            text = "User ${state.post.userId}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                    )

                    Text(
                        text = state.post.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                    )

                    Text(
                        text = state.post.body,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (state.comments.isNotEmpty()) {
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                        )
                        HorizontalDivider()
                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )

                        Text(
                            text = "Комментарии (${state.comments.size})",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )

                        state.comments.take(3).forEach { comment ->
                            CommentItem(comment)
                            Spacer(
                                modifier = Modifier
                                    .height(4.dp)
                            )
                        }

                        if (state.comments.size > 3) {
                            Text(
                                text = "и еще ${state.comments.size - 3}...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )
                        Text(
                            text = "Комментарии не загружены",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            is PostLoadState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ошибка: ${state.message}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "${comment.name}: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = comment.body,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
