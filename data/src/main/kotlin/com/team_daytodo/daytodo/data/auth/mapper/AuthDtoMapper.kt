package com.team_daytodo.daytodo.data.auth.mapper

import com.team_daytodo.daytodo.data.auth.local.AuthTokenValues
import com.team_daytodo.daytodo.data.auth.remote.dto.EmailCheckResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LinkNaverRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LinkNaverResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LoginNaverRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LoginNaverResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LoginRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LoginResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.LogoutRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.PasswordResetRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.PasswordResetRequestRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.RegisterRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.RegisterResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.TokenRefreshRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.TokenRefreshResponseDto
import com.team_daytodo.daytodo.data.auth.remote.dto.VerifyEmailResendRequestDto
import com.team_daytodo.daytodo.data.auth.remote.dto.VerifyEmailResponseDto
import com.team_daytodo.daytodo.domain.auth.model.EmailCheckRequest
import com.team_daytodo.daytodo.domain.auth.model.EmailCheckResult
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverRequest
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverResult
import com.team_daytodo.daytodo.domain.auth.model.LoginRequest
import com.team_daytodo.daytodo.domain.auth.model.LoginResult
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginRequest
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginResult
import com.team_daytodo.daytodo.domain.auth.model.ResendEmailVerificationRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordResult
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeResult
import com.team_daytodo.daytodo.domain.auth.model.SignupRequest
import com.team_daytodo.daytodo.domain.auth.model.SignupResult
import com.team_daytodo.daytodo.domain.auth.model.VerifyEmailRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyEmailResult
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeResult

fun LoginRequest.toDto(): LoginRequestDto =
    LoginRequestDto(
        email = email.trim(),
        password = password,
    )

fun NaverLoginRequest.toDto(): LoginNaverRequestDto =
    LoginNaverRequestDto(naverAccessToken = naverAccessToken)

fun SignupRequest.toRegisterDto(): RegisterRequestDto =
    RegisterRequestDto(
        email = email.trim(),
        password = password,
        nickname = nickname?.trim()?.takeIf(String::isNotBlank),
    )

fun LinkNaverRequest.toDto(): LinkNaverRequestDto =
    LinkNaverRequestDto(
        provider = provider,
        providerToken = providerToken,
    )

fun SendPasswordVerificationCodeRequest.toDto(): PasswordResetRequestRequestDto =
    PasswordResetRequestRequestDto(email = email.trim())

fun ResendEmailVerificationRequest.toDto(): VerifyEmailResendRequestDto =
    VerifyEmailResendRequestDto(email = email.trim())

fun EmailCheckRequest.toQueryEmail(): String =
    email.trim()

fun VerifyEmailRequest.toQueryToken(): String =
    token.trim()

fun VerifyPasswordCodeRequest.toVerifyPasswordCodeResult(): VerifyPasswordCodeResult =
    VerifyPasswordCodeResult(
        verificationToken = PasswordResetTokenCodec.encode(
            email = email,
            code = code,
        ),
    )

fun ResetPasswordRequest.toPasswordResetDto(): PasswordResetRequestDto {
    val token = PasswordResetTokenCodec.decode(verificationToken)

    return PasswordResetRequestDto(
        email = token.email,
        code = token.code,
        newPassword = newPassword,
    )
}

fun String.toTokenRefreshDto(): TokenRefreshRequestDto =
    TokenRefreshRequestDto(refreshToken = this)

fun String.toLogoutDto(): LogoutRequestDto =
    LogoutRequestDto(refreshToken = this)

fun LoginResponseDto.toLoginResult(): LoginResult =
    LoginResult(needsProfileSetup = user.nickname.isBlank())

fun LoginNaverResponseDto.toNaverLoginResult(): NaverLoginResult =
    NaverLoginResult(
        isNewUser = isNewUser,
        needsProfileSetup = isNewUser || user.nickname.isBlank(),
    )

fun LoginResponseDto.toTokenValues(): AuthTokenValues =
    AuthTokenValues(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )

fun LoginNaverResponseDto.toTokenValues(): AuthTokenValues =
    AuthTokenValues(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )

fun TokenRefreshResponseDto.toTokenValues(): AuthTokenValues =
    AuthTokenValues(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )

fun RegisterResponseDto.toSignupResult(): SignupResult =
    SignupResult(needsProfileSetup = nickname.isBlank())

fun LinkNaverResponseDto.toLinkNaverResult(): LinkNaverResult =
    LinkNaverResult(
        socialAccountId = socialAccountId,
        provider = provider,
        linkedAt = linkedAt,
    )

fun EmailCheckResponseDto.toEmailCheckResult(): EmailCheckResult =
    EmailCheckResult(
        email = email,
        available = available,
    )

fun VerifyEmailResponseDto.toVerifyEmailResult(): VerifyEmailResult =
    VerifyEmailResult(
        email = email,
        verified = verified,
    )

fun Unit.toSendPasswordVerificationCodeResult(): SendPasswordVerificationCodeResult =
    SendPasswordVerificationCodeResult(expiresInSeconds = DefaultVerificationCodeExpiresInSeconds)

fun Unit.toResetPasswordResult(): ResetPasswordResult =
    ResetPasswordResult(changed = true)

private object PasswordResetTokenCodec {
    fun encode(
        email: String,
        code: String,
    ): String = "${email.trim()}$PasswordResetTokenSeparator$code"

    fun decode(rawToken: String): PasswordResetToken {
        val values = rawToken.split(PasswordResetTokenSeparator, limit = 2)
        require(values.size == 2) { "Invalid password reset token." }

        return PasswordResetToken(
            email = values.first().trim(),
            code = values.last(),
        )
    }
}

private data class PasswordResetToken(
    val email: String,
    val code: String,
)

private const val PasswordResetTokenSeparator = "::"
private const val DefaultVerificationCodeExpiresInSeconds = 600
