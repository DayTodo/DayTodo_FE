package com.team_daytodo.daytodo.domain.auth.usecase

import com.team_daytodo.daytodo.domain.auth.model.InvalidAuthRequestException
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject

class VerifyPasswordCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: VerifyPasswordCodeRequest): Result<VerifyPasswordCodeResult> {
        val validationError = request.validateOrNull()
        if (validationError != null) return Result.failure(validationError)

        return authRepository.verifyPasswordCode(request)
    }
}

private fun VerifyPasswordCodeRequest.validateOrNull(): InvalidAuthRequestException? =
    when {
        !email.isValidEmail() -> InvalidAuthRequestException("올바른 이메일을 입력해 주세요.")
        !code.matches(VerificationCodeRegex) -> {
            InvalidAuthRequestException("인증코드 6자리를 입력해 주세요.")
        }
        else -> null
    }

private val VerificationCodeRegex = Regex("^\\d{6}$")
