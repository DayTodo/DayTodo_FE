package com.team_daytodo.daytodo.domain.home.model

sealed class HomeException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)

class HomeUnauthorizedException(
    cause: Throwable? = null,
) : HomeException(
    message = "인증이 필요합니다.",
    cause = cause,
)

class HomeLoadException(
    cause: Throwable? = null,
) : HomeException(
    message = "홈 정보를 불러오지 못했어요. 잠시 후 다시 시도해 주세요.",
    cause = cause,
)
