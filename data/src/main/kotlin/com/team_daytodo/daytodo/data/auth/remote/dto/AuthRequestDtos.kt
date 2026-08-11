package com.team_daytodo.daytodo.data.auth.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)

@Serializable
data class LoginNaverRequestDto(
    @SerialName("naverAccessToken")
    val naverAccessToken: String,
)

@Serializable
data class LogoutRequestDto(
    @SerialName("refreshToken")
    val refreshToken: String,
)

@Serializable
data class TokenRefreshRequestDto(
    @SerialName("refreshToken")
    val refreshToken: String,
)

@Serializable
data class LinkNaverRequestDto(
    @SerialName("provider")
    val provider: String,
    @SerialName("providerToken")
    val providerToken: String,
)

@Serializable
data class RegisterRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("nickname")
    val nickname: String? = null,
)

@Serializable
data class VerifyEmailResendRequestDto(
    @SerialName("email")
    val email: String,
)

@Serializable
data class PasswordResetRequestRequestDto(
    @SerialName("email")
    val email: String,
)

@Serializable
data class PasswordResetRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("code")
    val code: String,
    @SerialName("newPassword")
    val newPassword: String,
)