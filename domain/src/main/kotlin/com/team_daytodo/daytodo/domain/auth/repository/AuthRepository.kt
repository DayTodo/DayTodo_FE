package com.team_daytodo.daytodo.domain.auth.repository

import com.team_daytodo.daytodo.domain.auth.model.LoginRequest
import com.team_daytodo.daytodo.domain.auth.model.LoginResult
import com.team_daytodo.daytodo.domain.auth.model.AutoLoginResult
import com.team_daytodo.daytodo.domain.auth.model.EmailCheckRequest
import com.team_daytodo.daytodo.domain.auth.model.EmailCheckResult
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverRequest
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverResult
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginRequest
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginResult
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupRequest
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupResult
import com.team_daytodo.daytodo.domain.auth.model.ResendEmailVerificationRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordResult
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeResult
import com.team_daytodo.daytodo.domain.auth.model.SignupRequest
import com.team_daytodo.daytodo.domain.auth.model.SignupResult
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeResult
import com.team_daytodo.daytodo.domain.auth.model.VerifyEmailRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyEmailResult

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResult>

    suspend fun loginWithNaver(request: NaverLoginRequest): Result<NaverLoginResult>

    suspend fun checkAutoLogin(): Result<AutoLoginResult>

    suspend fun logout(): Result<Unit>

    suspend fun signup(request: SignupRequest): Result<SignupResult>

    suspend fun linkNaver(request: LinkNaverRequest): Result<LinkNaverResult>

    suspend fun checkEmail(request: EmailCheckRequest): Result<EmailCheckResult>

    suspend fun verifyEmail(request: VerifyEmailRequest): Result<VerifyEmailResult>

    suspend fun resendEmailVerification(request: ResendEmailVerificationRequest): Result<Unit>

    suspend fun sendPasswordVerificationCode(
        request: SendPasswordVerificationCodeRequest,
    ): Result<SendPasswordVerificationCodeResult>

    suspend fun verifyPasswordCode(
        request: VerifyPasswordCodeRequest,
    ): Result<VerifyPasswordCodeResult>

    suspend fun resetPassword(request: ResetPasswordRequest): Result<ResetPasswordResult>

    suspend fun saveProfile(request: ProfileSetupRequest): Result<ProfileSetupResult>
}
