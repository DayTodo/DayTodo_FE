package com.team_daytodo.daytodo.domain.auth.usecase

import com.team_daytodo.daytodo.domain.auth.repository.AuthTokenRepository
import javax.inject.Inject

class GetRefreshTokenUseCase @Inject constructor(
    private val authTokenRepository: AuthTokenRepository,
) {
    suspend operator fun invoke(): String? = authTokenRepository.getRefreshToken()
}
