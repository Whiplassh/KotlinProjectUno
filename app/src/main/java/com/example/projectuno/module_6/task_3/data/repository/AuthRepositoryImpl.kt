package com.example.projectuno.module_6.task_3.data.repository

import com.example.projectuno.module_6.task_3.data.local.TokenManager
import com.example.projectuno.module_6.task_3.data.remote.KtorClient
import com.example.projectuno.module_6.task_3.data.remote.dto.LoginResponse
import com.example.projectuno.module_6.task_3.data.remote.dto.UserDto
import com.example.projectuno.module_6.task_3.data.remote.dto.UsersListResponse
import com.example.projectuno.module_6.task_3.domain.model.User
import com.example.projectuno.module_6.task_3.domain.repository.AuthRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl(
    private val tokenManager: TokenManager
) : AuthRepository {
    private val baseUrl = "https://dummyjson.com"

    override suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = KtorClient.client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("username" to username, "password" to password))
            }
            
            if (response.status == HttpStatusCode.OK) {
                val loginResponse: LoginResponse = response.body()
                tokenManager.saveToken(loginResponse.token)
                Result.success(loginResponse.token)
            } else {
                Result.failure(Exception("Неверный логин или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val token = tokenManager.token.first()
            val response: UsersListResponse = KtorClient.client.get("$baseUrl/users") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
            Result.success(response.users.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserDetail(id: Int): Result<User> {
        return try {
            val token = tokenManager.token.first()
            val response: UserDto = KtorClient.client.get("$baseUrl/users/$id") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getToken(): Flow<String?> = tokenManager.token

    override suspend fun logout() {
        tokenManager.deleteToken()
    }

    private fun UserDto.toDomain() = User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        username = username,
        email = email,
        image = image
    )
}
