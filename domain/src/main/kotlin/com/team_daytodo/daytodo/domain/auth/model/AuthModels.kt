package com.team_daytodo.daytodo.domain.auth.model

/** 로그인 */
data class LoginRequest(
    val email: String,
    val password: String,
    val keepLoggedIn: Boolean,
)

data class LoginResult(
    val needsProfileSetup: Boolean,
)

/** 네이버 로그인 */
data class NaverLoginRequest(
    val naverAccessToken: String,
    val keepLoggedIn: Boolean,
)

data class NaverLoginResult(
    val isNewUser: Boolean,
    val needsProfileSetup: Boolean,
)

/** 회원가입 */
data class SignupRequest(
    val email: String,
    val password: String,
    val agreedToTerms: Boolean,
    val nickname: String? = null,
)

data class SignupResult(
    val needsProfileSetup: Boolean,
)

/** 자동 로그인 */
data class AutoLoginResult(
    val isLoggedIn: Boolean,
    val usedCachedSession: Boolean = false,
)

/** 네이버 계정 연동 */
data class LinkNaverRequest(
    val providerToken: String,
    val provider: String = NaverProvider,
)

data class LinkNaverResult(
    val socialAccountId: Int,
    val provider: String,
    val linkedAt: String,
)

/** 이메일 중복 확인 */
data class EmailCheckRequest(
    val email: String,
)

data class EmailCheckResult(
    val email: String,
    val available: Boolean,
)

/** 이메일 인증 */
data class VerifyEmailRequest(
    val token: String,
)

data class VerifyEmailResult(
    val email: String,
    val verified: Boolean,
)

/** 인증 메일 재전송 */
data class ResendEmailVerificationRequest(
    val email: String,
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

/** 비밀번호 재설정 */
data class ResetPasswordRequest(
    val verificationToken: String,
    val newPassword: String,
)

data class ResetPasswordResult(
    val changed: Boolean,
)

/** 프로필 설정 */
data class ProfileSetupRequest(
    val nickname: String,
    val profileImageUri: String?,
)

data class ProfileSetupResult(
    val nickname: String,
)

private const val NaverProvider = "NAVER"