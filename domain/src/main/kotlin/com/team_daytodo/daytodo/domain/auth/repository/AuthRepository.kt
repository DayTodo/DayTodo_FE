package com.team_daytodo.daytodo.domain.auth.repository

import com.team_daytodo.daytodo.domain.auth.model.LoginRequest
import com.team_daytodo.daytodo.domain.auth.model.LoginResult
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupRequest
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupResult
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordRequest
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordResult
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeResult
import com.team_daytodo.daytodo.domain.auth.model.SignupRequest
import com.team_daytodo.daytodo.domain.auth.model.SignupResult
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeResult

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResult>

    suspend fun signup(request: SignupRequest): Result<SignupResult>

    suspend fun sendPasswordVerificationCode(
        request: SendPasswordVerificationCodeRequest,
    ): Result<SendPasswordVerificationCodeResult>

    suspend fun verifyPasswordCode(
        request: VerifyPasswordCodeRequest,
    ): Result<VerifyPasswordCodeResult>

    suspend fun resetPassword(request: ResetPasswordRequest): Result<ResetPasswordResult>

    suspend fun saveProfile(request: ProfileSetupRequest): Result<ProfileSetupResult>
}
