package com.team_daytodo.daytodo.domain.auth.model

open class AuthException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

class InvalidAuthRequestException(
    message: String,
) : AuthException(message)

class AuthLoginException(
    cause: Throwable? = null,
) : AuthException("로그인에 실패했어요. 잠시 후 다시 시도해 주세요.", cause)

class AuthSignupException(
    cause: Throwable? = null,
) : AuthException("회원가입에 실패했어요. 잠시 후 다시 시도해 주세요.", cause)

class PasswordVerificationException(
    message: String = "인증코드를 확인하지 못했어요. 다시 시도해 주세요.",
    cause: Throwable? = null,
) : AuthException(message, cause)

class ResetPasswordException(
    cause: Throwable? = null,
) : AuthException("비밀번호를 변경하지 못했어요. 잠시 후 다시 시도해 주세요.", cause)

class ProfileSetupException(
    cause: Throwable? = null,
) : AuthException("프로필을 저장하지 못했어요. 잠시 후 다시 시도해 주세요.", cause)
