package com.team_daytodo.daytodo.domain.auth.model

data class LoginRequest(
    val email: String,
    val password: String,
    val keepLoggedIn: Boolean,
)

data class LoginResult(
    val needsProfileSetup: Boolean,
)

data class SignupRequest(
    val email: String,
    val password: String,
    val agreedToTerms: Boolean,
)

data class SignupResult(
    val needsProfileSetup: Boolean,
)

data class SendPasswordVerificationCodeRequest(
    val email: String,
)

data class SendPasswordVerificationCodeResult(
    val expiresInSeconds: Int,
)

data class VerifyPasswordCodeRequest(
    val email: String,
    val code: String,
)

data class VerifyPasswordCodeResult(
    val verificationToken: String,
)

data class ResetPasswordRequest(
    val verificationToken: String,
    val newPassword: String,
)

data class ResetPasswordResult(
    val changed: Boolean,
)

data class ProfileSetupRequest(
    val nickname: String,
    val profileImageUri: String?,
)

data class ProfileSetupResult(
    val nickname: String,
)
