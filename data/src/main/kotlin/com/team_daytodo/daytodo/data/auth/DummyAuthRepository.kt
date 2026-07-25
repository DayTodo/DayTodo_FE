package com.team_daytodo.daytodo.data.auth

import com.team_daytodo.daytodo.domain.auth.model.AuthLoginException
import com.team_daytodo.daytodo.domain.auth.model.AuthSignupException
import com.team_daytodo.daytodo.domain.auth.model.LoginRequest
import com.team_daytodo.daytodo.domain.auth.model.LoginResult
import com.team_daytodo.daytodo.domain.auth.model.PasswordVerificationException
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupException
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupRequest
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupResult
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordException
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordResult
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeResult
import com.team_daytodo.daytodo.domain.auth.model.SignupRequest
import com.team_daytodo.daytodo.domain.auth.model.SignupResult
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeResult
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyAuthRepository @Inject constructor() : AuthRepository {
    private val signedUpEmails = mutableSetOf(
        "demo@daytodo.com",
        "daytodo@daytodo.com",
    )
    private val profileCompletedEmails = mutableSetOf(
        "demo@daytodo.com",
        "daytodo@daytodo.com",
    )
    private var currentEmail: String? = null

    override suspend fun login(request: LoginRequest): Result<LoginResult> =
        runCatching {
            delay(AuthRequestDelayMillis)

            val normalizedEmail = request.email.trim().lowercase()
            signedUpEmails += normalizedEmail
            currentEmail = normalizedEmail

            LoginResult(
                needsProfileSetup = normalizedEmail !in profileCompletedEmails,
            )
        }.recoverCatching { cause ->
            throw AuthLoginException(cause)
        }

    override suspend fun signup(request: SignupRequest): Result<SignupResult> =
        runCatching {
            delay(AuthRequestDelayMillis)

            val normalizedEmail = request.email.trim().lowercase()
            signedUpEmails += normalizedEmail
            currentEmail = normalizedEmail

            SignupResult(needsProfileSetup = true)
        }.recoverCatching { cause ->
            throw AuthSignupException(cause)
        }

    override suspend fun sendPasswordVerificationCode(
        request: SendPasswordVerificationCodeRequest,
    ): Result<SendPasswordVerificationCodeResult> =
        runCatching {
            delay(AuthRequestDelayMillis)

            val normalizedEmail = request.email.trim().lowercase()
            if (normalizedEmail !in signedUpEmails) {
                signedUpEmails += normalizedEmail
            }

            SendPasswordVerificationCodeResult(
                expiresInSeconds = VerificationCodeExpiresInSeconds,
            )
        }.recoverCatching { cause ->
            throw PasswordVerificationException(cause = cause)
        }

    override suspend fun verifyPasswordCode(
        request: VerifyPasswordCodeRequest,
    ): Result<VerifyPasswordCodeResult> =
        runCatching {
            delay(AuthRequestDelayMillis)

            if (request.code != DummyVerificationCode) {
                throw PasswordVerificationException("인증코드가 일치하지 않아요. 더미 코드는 123456이에요.")
            }

            VerifyPasswordCodeResult(
                verificationToken = "dummy-password-token-${request.email.hashCode().toUInt()}",
            )
        }.recoverCatching { cause ->
            if (cause is PasswordVerificationException) throw cause
            throw PasswordVerificationException(cause = cause)
        }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<ResetPasswordResult> =
        runCatching {
            delay(AuthRequestDelayMillis)

            ResetPasswordResult(changed = request.verificationToken.isNotBlank())
        }.recoverCatching { cause ->
            throw ResetPasswordException(cause)
        }

    override suspend fun saveProfile(request: ProfileSetupRequest): Result<ProfileSetupResult> =
        runCatching {
            delay(AuthRequestDelayMillis)

            currentEmail?.let { profileCompletedEmails += it }
            ProfileSetupResult(nickname = request.nickname)
        }.recoverCatching { cause ->
            throw ProfileSetupException(cause)
        }

    private companion object {
        const val AuthRequestDelayMillis = 700L
        const val DummyVerificationCode = "123456"
        const val VerificationCodeExpiresInSeconds = 180
    }
}
