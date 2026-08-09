package com.team_daytodo.daytodo.domain.mypage.model

sealed class FeedbackException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)

class FeedbackUnauthorizedException(cause: Throwable? = null) : FeedbackException(
    message = "로그인이 필요해요.",
    cause = cause,
)

class FeedbackTooShortException(cause: Throwable? = null) : FeedbackException(
    message = "의견은 공백을 제외하고 100자 이상 입력해 주세요.",
    cause = cause,
)

class FeedbackSubmitFailedException(cause: Throwable? = null) : FeedbackException(
    message = "의견을 전달하지 못했어요. 잠시 후 다시 시도해 주세요.",
    cause = cause,
)
