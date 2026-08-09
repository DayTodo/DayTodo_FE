package com.team_daytodo.daytodo.domain.mypage.model

sealed class ChangePasswordException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)

class InvalidCurrentPasswordException(cause: Throwable? = null) : ChangePasswordException(
    message = "현재 비밀번호가 일치하지 않아요.",
    cause = cause,
)

class SocialAccountPasswordChangeNotAllowedException(cause: Throwable? = null) : ChangePasswordException(
    message = "소셜 로그인 계정은 비밀번호를 변경할 수 없어요.",
    cause = cause,
)

class ChangePasswordFailedException(cause: Throwable? = null) : ChangePasswordException(
    message = "비밀번호를 변경하지 못했어요. 잠시 후 다시 시도해 주세요.",
    cause = cause,
)
