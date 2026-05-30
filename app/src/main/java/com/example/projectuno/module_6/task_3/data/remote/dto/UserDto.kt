package com.example.projectuno.module_6.task_3.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val image: String
)

@Serializable
data class UsersListResponse(
    val users: List<UserDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@Serializable
data class LoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    @SerialName("accessToken")
    val token: String
)
