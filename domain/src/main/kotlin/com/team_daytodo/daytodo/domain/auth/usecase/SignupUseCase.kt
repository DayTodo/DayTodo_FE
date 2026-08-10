package com.team_daytodo.daytodo.domain.auth.usecase

import com.team_daytodo.daytodo.domain.auth.model.InvalidAuthRequestException
import com.team_daytodo.daytodo.domain.auth.model.SignupRequest
import com.team_daytodo.daytodo.domain.auth.model.SignupResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: SignupRequest): Result<SignupResult> {
        val validationError = request.validateOrNull()
        if (validationError != null) return Result.failure(validationError)

        return authRepository.signup(request)
    }
}

private fun SignupRequest.validateOrNull(): InvalidAuthRequestException? =
    when {
        !email.isValidEmail() -> InvalidAuthRequestException("올바른 이메일을 입력해 주세요.")
        !password.isValidPassword() -> {
            InvalidAuthRequestException("비밀번호는 영문과 숫자를 포함해 8자 이상이어야 해요.")
        }
        !agreedToTerms -> InvalidAuthRequestException("이용약관 및 개인정보처리방침에 동의해 주세요.")
        else -> null
    }
