package com.example.projectuno.module_4.task_13

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Message(
    val id: Int,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MessageRepository {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private var messageCounter = 0

    fun coldFlow(): Flow<String> = flow {
        println("Cold Flow started")
        emit("Message 1")
        delay(1000)
        emit("Message 2")
        delay(1000)
        emit("Message 3")
    }

    fun hotFlow(): SharedFlow<String> = flow {
        var counter = 0
        while (true) {
            emit("Hot message ${++counter}")
            delay(2000)
        }
    }.shareIn(
        scope = kotlinx.coroutines.GlobalScope,
        started = SharingStarted.Lazily,
        replay = 1
    )

    fun addMessage(text: String) {
        val newMessage = Message(
            id = ++messageCounter,
            text = text
        )
        _messages.value = _messages.value + newMessage
    }

    fun clearMessages() {
        _messages.value = emptyList()
        messageCounter = 0
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowDemoScreen() {
    val repository = remember { MessageRepository() }
    val scope = rememberCoroutineScope()

    var coldFlowMessages by remember { mutableStateOf<List<String>>(emptyList()) }
    var hotFlowMessage by remember { mutableStateOf("Waiting for hot flow...") }
    var isCollectingCold by remember { mutableStateOf(false) }
    var isCollectingHot by remember { mutableStateOf(false) }

    val messages by repository.messages.collectAsState()

    var newMessageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Flow Demo",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Cold Flow Demo",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "Cold Flow запускается заново для каждого подписчика",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

                coldFlowMessages.forEach { message ->
                    Text(
                        text = "• $message",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                    )
                }

                Button(
                    onClick = {
                        if (!isCollectingCold) {
                            isCollectingCold = true
                            coldFlowMessages = emptyList()
                            scope.launch {
                                repository.coldFlow().collect { message ->
                                    coldFlowMessages = coldFlowMessages + message
                                }
                                isCollectingCold = false
                            }
                        }
                    },
                    enabled = !isCollectingCold,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(if (isCollectingCold) "Collecting..." else "Start Cold Flow")
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hot Flow Demo",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Hot Flow работает независимо от подписчиков",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = hotFlowMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (!isCollectingHot) {
                                isCollectingHot = true
                                scope.launch {
                                    repository.hotFlow().collect { message ->
                                        hotFlowMessage = message
                                    }
                                }
                            }
                        },
                        enabled = !isCollectingHot
                    ) {
                        Text(if (isCollectingHot) "Collecting..." else "Start Hot Flow")
                    }

                    if (isCollectingHot) {
                        Button(
                            onClick = {
                                isCollectingHot = false
                                hotFlowMessage = "Stopped"
                            }
                        ) {
                            Text("Stop")
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                .padding(16.dp)) {
                Text(
                    text = "StateFlow Demo",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "StateFlow хранит текущее состояние",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newMessageText,
                        onValueChange = { newMessageText = it },
                        label = { Text("New message") },
                        modifier = Modifier
                            .weight(1f),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (newMessageText.isNotBlank()) {
                                repository.addMessage(newMessageText)
                                newMessageText = ""
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text("Add")
                    }
                }

                if (messages.isNotEmpty()) {
                    Button(
                        onClick = { repository.clearMessages() },
                        modifier = Modifier
                            .padding(top = 8.dp)
                    ) {
                        Text("Clear All")
                    }
                }
            }
        }

        if (messages.isNotEmpty()) {
            Text(
                text = "Messages (${messages.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { message ->
                    MessageCard(message)
                }
            }
        }
    }
}

@Composable
fun MessageCard(message: Message) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "ID: ${message.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
