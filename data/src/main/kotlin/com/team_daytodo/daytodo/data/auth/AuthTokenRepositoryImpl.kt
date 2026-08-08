package com.team_daytodo.daytodo.data.auth

import com.team_daytodo.daytodo.data.auth.local.AuthTokenLocalDataSource
import com.team_daytodo.daytodo.domain.auth.repository.AuthTokenRepository
import javax.inject.Inject

class AuthTokenRepositoryImpl @Inject constructor(
    private val authTokenLocalDataSource: AuthTokenLocalDataSource,
) : AuthTokenRepository {
    override suspend fun getRefreshToken(): String? = authTokenLocalDataSource.getRefreshToken()

    override suspend fun clearTokens() {
        authTokenLocalDataSource.clearAccessToken()
        authTokenLocalDataSource.clearRefreshToken()
    }
}
