package com.example.projectuno.module_6.task_3.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectuno.module_6.task_3.domain.model.User
import com.example.projectuno.module_6.task_3.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<User>) : UsersState()
    data class Error(val message: String) : UsersState()
}

sealed class DetailState {
    object Loading : DetailState()
    data class Success(val user: User) : DetailState()
    data class Error(val message: String) : DetailState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _usersState = MutableStateFlow<UsersState>(UsersState.Loading)
    val usersState: StateFlow<UsersState> = _usersState.asStateFlow()

    private val _detailState = MutableStateFlow<DetailState>(DetailState.Loading)
    val detailState: StateFlow<DetailState> = _detailState.asStateFlow()

    val token = repository.getToken()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.login(username, password)
                .onSuccess { _authState.value = AuthState.Success(it) }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Unknown error") }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UsersState.Loading
            repository.getUsers()
                .onSuccess { _usersState.value = UsersState.Success(it) }
                .onFailure { _usersState.value = UsersState.Error(it.message ?: "Unknown error") }
        }
    }

    fun loadUserDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = DetailState.Loading
            repository.getUserDetail(id)
                .onSuccess { _detailState.value = DetailState.Success(it) }
                .onFailure { _detailState.value = DetailState.Error(it.message ?: "Unknown error") }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }
}
