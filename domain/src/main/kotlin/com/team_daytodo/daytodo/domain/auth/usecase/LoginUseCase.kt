package com.team_daytodo.daytodo.domain.auth.usecase

import com.team_daytodo.daytodo.domain.auth.model.InvalidAuthRequestException
import com.team_daytodo.daytodo.domain.auth.model.LoginRequest
import com.team_daytodo.daytodo.domain.auth.model.LoginResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: LoginRequest): Result<LoginResult> {
        val validationError = request.validateOrNull()
        if (validationError != null) return Result.failure(validationError)

        return authRepository.login(request)
    }
}

private fun LoginRequest.validateOrNull(): InvalidAuthRequestException? =
    when {
        !email.isValidEmail() -> InvalidAuthRequestException("올바른 이메일을 입력해 주세요.")
        password.isBlank() -> InvalidAuthRequestException("비밀번호를 입력해 주세요.")
        else -> null
    }
