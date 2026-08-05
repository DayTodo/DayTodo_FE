package com.team_daytodo.daytodo.data.auth.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("user")
    val user: UserDto,
)

@Serializable
data class LoginNaverResponseDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("isNewUser")
    val isNewUser: Boolean,
    @SerialName("user")
    val user: UserDto,
)

@Serializable
data class UserDto(
    @SerialName("userId")
    val userId: Int,
    @SerialName("nickname")
    val nickname: String,
)

@Serializable
data class TokenRefreshResponseDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
)

@Serializable
data class LinkNaverResponseDto(
    @SerialName("socialAccountId")
    val socialAccountId: Int,
    @SerialName("provider")
    val provider: String,
    @SerialName("linkedAt")
    val linkedAt: String,
)

@Serializable
data class EmailCheckResponseDto(
    @SerialName("email")
    val email: String,
    @SerialName("available")
    val available: Boolean,
)

@Serializable
data class RegisterResponseDto(
    @SerialName("userId")
    val userId: Int,
    @SerialName("email")
    val email: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("createdAt")
    val createdAt: String,
)

@Serializable
data class VerifyEmailResponseDto(
    @SerialName("email")
    val email: String,
    @SerialName("verified")
    val verified: Boolean,
)