package com.team_daytodo.daytodo.domain.auth.usecase

import com.team_daytodo.daytodo.domain.auth.model.InvalidAuthRequestException
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SendPasswordVerificationCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        request: SendPasswordVerificationCodeRequest,
    ): Result<SendPasswordVerificationCodeResult> {
        if (!request.email.isValidEmail()) {
            return Result.failure(InvalidAuthRequestException("올바른 이메일을 입력해 주세요."))
        }

        return authRepository.sendPasswordVerificationCode(request)
    }
}
