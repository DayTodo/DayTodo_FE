package com.team_daytodo.daytodo.domain.auth.repository

interface AuthTokenRepository {
    suspend fun getRefreshToken(): String?

    suspend fun clearTokens()
}
