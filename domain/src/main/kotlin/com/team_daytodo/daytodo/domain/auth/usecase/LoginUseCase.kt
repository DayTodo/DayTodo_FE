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
        !email.isValidEmail() -> InvalidAuthRequestException(
            "\uc62c\ubc14\ub978 \uc774\uba54\uc77c\uc744 \uc785\ub825\ud574 \uc8fc\uc138\uc694.",
        )
        password.isBlank() -> InvalidAuthRequestException(
            "\ube44\ubc00\ubc88\ud638\ub97c \uc785\ub825\ud574 \uc8fc\uc138\uc694.",
        )
        !password.isValidPassword() -> InvalidAuthRequestException(
            "\ube44\ubc00\ubc88\ud638\ub294 \uc601\ubb38\uacfc \uc22b\uc790\ub97c \ud3ec\ud568\ud574 8\uc790 \uc774\uc0c1\uc774\uc5b4\uc57c \ud574\uc694.",
        )
        else -> null
    }
