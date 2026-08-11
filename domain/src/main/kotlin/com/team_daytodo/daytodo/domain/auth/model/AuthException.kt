package com.team_daytodo.daytodo.domain.auth.model

import com.team_daytodo.daytodo.core.model.DayTodoException

enum class AuthErrorCode {
    INVALID_REQUEST,
    LOGIN_FAILED,
    SIGNUP_FAILED,
    EMAIL_ALREADY_EXISTS,
    PASSWORD_VERIFICATION_FAILED,
    RESET_PASSWORD_FAILED,
    PROFILE_SETUP_FAILED,
    NAVER_LINK_FAILED,
    EMAIL_CHECK_FAILED,
    EMAIL_VERIFICATION_FAILED,
    LOGOUT_FAILED,
    AUTO_LOGIN_FAILED,

    VALIDATION_ERROR,
    INVALID_VERIFICATION_TOKEN,
    EXPIRED_VERIFICATION_TOKEN,
    INVALID_RESET_CODE,
    EXPIRED_RESET_CODE,
    INVALID_CREDENTIALS,
    INVALID_REFRESH_TOKEN,
    UNAUTHORIZED,
    EMAIL_NOT_VERIFIED,
    WITHDRAWN_USER,
    EMAIL_NOT_FOUND,
    ALREADY_LINKED_SAME_PROVIDER,
    SOCIAL_ACCOUNT_ALREADY_LINKED,
    EMAIL_DUPLICATED,
    ALREADY_VERIFIED,
    VERIFICATION_CODE_EXPIRED,
    ACCOUNT_LOCKED,
    INTERNAL_SERVER_ERROR,
    NAVER_API_ERROR,
}

open class AuthException(
    val authErrorCode: AuthErrorCode,
    message: String,
    cause: Throwable? = null,
) : DayTodoException(
    errorCode = authErrorCode.name,
    userMessage = message,
    cause = cause,
)

class AuthServerException(
    authErrorCode: AuthErrorCode,
    message: String,
    cause: Throwable? = null,
) : AuthException(
    authErrorCode = authErrorCode,
    message = message,
    cause = cause,
)

class InvalidAuthRequestException(
    message: String,
) : AuthException(
    authErrorCode = AuthErrorCode.INVALID_REQUEST,
    message = message,
)

class AuthLoginException(
    cause: Throwable? = null,
    message: String = "로그인에 실패했어요. 잠시 후 다시 시도해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.LOGIN_FAILED,
    message = message,
    cause = cause,
)

class AccountLockedException(
    cause: Throwable? = null,
    message: String = "로그인 실패 횟수 초과로 계정이 잠겼습니다.",
) : AuthException(
    authErrorCode = AuthErrorCode.ACCOUNT_LOCKED,
    message = message,
    cause = cause,
)

class AuthSignupException(
    cause: Throwable? = null,
    message: String = "회원가입에 실패했어요. 잠시 후 다시 시도해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.SIGNUP_FAILED,
    message = message,
    cause = cause,
)

class EmailAlreadyExistsException(
    cause: Throwable? = null,
    message: String = "이미 가입된 이메일입니다.",
) : AuthException(
    authErrorCode = AuthErrorCode.EMAIL_ALREADY_EXISTS,
    message = message,
    cause = cause,
)

class PasswordVerificationException(
    message: String = "인증코드를 확인하지 못했어요. 다시 시도해 주세요.",
    cause: Throwable? = null,
) : AuthException(
    authErrorCode = AuthErrorCode.PASSWORD_VERIFICATION_FAILED,
    message = message,
    cause = cause,
)

class NaverLinkException(
    cause: Throwable? = null,
    message: String = "네이버 계정을 연동하지 못했어요. 잠시 후 다시 시도해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.NAVER_LINK_FAILED,
    message = message,
    cause = cause,
)

class EmailCheckException(
    cause: Throwable? = null,
    message: String = "이메일을 확인하지 못했어요. 잠시 후 다시 시도해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.EMAIL_CHECK_FAILED,
    message = message,
    cause = cause,
)

class EmailVerificationException(
    cause: Throwable? = null,
    message: String = "이메일 인증을 처리하지 못했어요. 잠시 후 다시 시도해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.EMAIL_VERIFICATION_FAILED,
    message = message,
    cause = cause,
)

class LogoutException(
    cause: Throwable? = null,
    message: String = "로그아웃을 완료하지 못했어요. 잠시 후 다시 시도해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.LOGOUT_FAILED,
    message = message,
    cause = cause,
)

class AutoLoginException(
    cause: Throwable? = null,
    message: String = "자동 로그인에 실패했어요. 다시 로그인해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.AUTO_LOGIN_FAILED,
    message = message,
    cause = cause,
)

class ResetPasswordException(
    cause: Throwable? = null,
    message: String = "비밀번호를 변경하지 못했어요. 잠시 후 다시 시도해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.RESET_PASSWORD_FAILED,
    message = message,
    cause = cause,
)

class ProfileSetupException(
    cause: Throwable? = null,
    message: String = "프로필을 저장하지 못했어요. 잠시 후 다시 시도해 주세요.",
) : AuthException(
    authErrorCode = AuthErrorCode.PROFILE_SETUP_FAILED,
    message = message,
    cause = cause,
)
