package com.team_daytodo.daytodo.domain.auth.usecase

import com.team_daytodo.daytodo.domain.auth.model.InvalidAuthRequestException
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupRequest
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: ProfileSetupRequest): Result<ProfileSetupResult> {
        val validationError = request.validateOrNull()
        if (validationError != null) return Result.failure(validationError)

        return authRepository.saveProfile(
            request.copy(nickname = request.nickname.trim()),
        )
    }
}

private fun ProfileSetupRequest.validateOrNull(): InvalidAuthRequestException? =
    when {
        nickname.isBlank() -> InvalidAuthRequestException("닉네임을 입력해 주세요.")
        nickname.trim().length > NicknameMaxLength -> {
            InvalidAuthRequestException("닉네임은 ${NicknameMaxLength}자 이하로 입력해 주세요.")
        }
        else -> null
    }

private const val NicknameMaxLength = 20
