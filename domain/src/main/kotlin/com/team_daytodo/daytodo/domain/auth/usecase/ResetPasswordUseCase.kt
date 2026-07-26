package com.team_daytodo.daytodo.domain.auth.usecase

import com.team_daytodo.daytodo.domain.auth.model.InvalidAuthRequestException
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: ResetPasswordRequest): Result<ResetPasswordResult> {
        val validationError = request.validateOrNull()
        if (validationError != null) return Result.failure(validationError)

        return authRepository.resetPassword(request)
    }
}

private fun ResetPasswordRequest.validateOrNull(): InvalidAuthRequestException? =
    when {
        verificationToken.isBlank() -> InvalidAuthRequestException("비밀번호 인증을 먼저 완료해 주세요.")
        !newPassword.isValidPassword() -> {
            InvalidAuthRequestException("비밀번호는 영문과 숫자를 포함해 8자 이상이어야 해요.")
        }
        else -> null
    }
