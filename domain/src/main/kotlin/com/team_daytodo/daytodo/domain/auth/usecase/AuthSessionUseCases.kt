package com.team_daytodo.daytodo.domain.auth.usecase

import com.team_daytodo.daytodo.domain.auth.model.AutoLoginResult
import com.team_daytodo.daytodo.domain.auth.model.InvalidAuthRequestException
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverRequest
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverResult
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginRequest
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginResult
import com.team_daytodo.daytodo.domain.auth.model.ResendEmailVerificationRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyEmailRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyEmailResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject

class CheckAutoLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<AutoLoginResult> =
        authRepository.checkAutoLogin()
}

class LoginWithNaverUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: NaverLoginRequest): Result<NaverLoginResult> {
        if (request.naverAccessToken.isBlank()) {
            return Result.failure(InvalidAuthRequestException("네이버 로그인 인증 정보가 필요해요."))
        }

        return authRepository.loginWithNaver(request)
    }
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> =
        authRepository.logout()
}

class LinkNaverUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: LinkNaverRequest): Result<LinkNaverResult> {
        if (request.providerToken.isBlank()) {
            return Result.failure(InvalidAuthRequestException("네이버 계정 연동 정보가 필요해요."))
        }

        return authRepository.linkNaver(request)
    }
}

class VerifyEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: VerifyEmailRequest): Result<VerifyEmailResult> {
        if (request.token.isBlank()) {
            return Result.failure(InvalidAuthRequestException("이메일 인증 토큰이 필요해요."))
        }

        return authRepository.verifyEmail(request)
    }
}

class ResendEmailVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: ResendEmailVerificationRequest): Result<Unit> {
        if (!request.email.isValidEmail()) {
            return Result.failure(InvalidAuthRequestException("올바른 이메일을 입력해 주세요."))
        }

        return authRepository.resendEmailVerification(request)
    }
}
