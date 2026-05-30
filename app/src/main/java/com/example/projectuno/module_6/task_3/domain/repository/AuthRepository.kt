package com.example.projectuno.module_6.task_3.domain.repository

import com.example.projectuno.module_6.task_3.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<String>
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUserDetail(id: Int): Result<User>
    fun getToken(): Flow<String?>
    suspend fun logout()
}
