package com.team_daytodo.daytodo.core.model

open class DayTodoException(
    val errorCode: String,
    val userMessage: String,
    cause: Throwable? = null,
) : RuntimeException(userMessage, cause)

sealed class CommonError(
    errorCode: String,
    userMessage: String,
    val statusCode: Int? = null,
    val serverCode: String? = null,
    cause: Throwable? = null,
) : DayTodoException(
    errorCode = errorCode,
    userMessage = userMessage,
    cause = cause,
) {
    class NetworkUnavailable(
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_NETWORK_UNAVAILABLE",
        userMessage = "인터넷 연결을 확인해 주세요.",
        cause = cause,
    )

    class Timeout(
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_TIMEOUT",
        userMessage = "요청 시간이 초과됐어요. 잠시 후 다시 시도해 주세요.",
        cause = cause,
    )

    class BadRequest(
        userMessage: String? = null,
        serverCode: String? = null,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_BAD_REQUEST",
        userMessage = userMessage ?: "요청 값을 확인해 주세요.",
        statusCode = 400,
        serverCode = serverCode,
        cause = cause,
    )

    class Unauthorized(
        userMessage: String? = null,
        serverCode: String? = null,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_UNAUTHORIZED",
        userMessage = userMessage ?: "로그인이 필요해요. 다시 로그인해 주세요.",
        statusCode = 401,
        serverCode = serverCode,
        cause = cause,
    )

    class Forbidden(
        userMessage: String? = null,
        serverCode: String? = null,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_FORBIDDEN",
        userMessage = userMessage ?: "접근 권한이 없어요.",
        statusCode = 403,
        serverCode = serverCode,
        cause = cause,
    )

    class NotFound(
        userMessage: String? = null,
        serverCode: String? = null,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_NOT_FOUND",
        userMessage = userMessage ?: "요청한 정보를 찾을 수 없어요.",
        statusCode = 404,
        serverCode = serverCode,
        cause = cause,
    )

    class Conflict(
        userMessage: String? = null,
        serverCode: String? = null,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_CONFLICT",
        userMessage = userMessage ?: "이미 처리된 요청이에요.",
        statusCode = 409,
        serverCode = serverCode,
        cause = cause,
    )

    class TooManyRequests(
        userMessage: String? = null,
        serverCode: String? = null,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_TOO_MANY_REQUESTS",
        userMessage = userMessage ?: "요청이 너무 많아요. 잠시 후 다시 시도해 주세요.",
        statusCode = 429,
        serverCode = serverCode,
        cause = cause,
    )

    class Server(
        statusCode: Int,
        userMessage: String? = null,
        serverCode: String? = null,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_SERVER_ERROR",
        userMessage = userMessage ?: "서버에 문제가 생겼어요. 잠시 후 다시 시도해 주세요.",
        statusCode = statusCode,
        serverCode = serverCode,
        cause = cause,
    )

    class Http(
        statusCode: Int,
        userMessage: String? = null,
        serverCode: String? = null,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_HTTP_ERROR",
        userMessage = userMessage ?: "요청을 처리하지 못했어요. 잠시 후 다시 시도해 주세요.",
        statusCode = statusCode,
        serverCode = serverCode,
        cause = cause,
    )

    class EmptyBody(
        val endpoint: String,
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_EMPTY_BODY",
        userMessage = "서버 응답을 확인하지 못했어요. 잠시 후 다시 시도해 주세요.",
        cause = cause,
    )

    class InvalidResponse(
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_INVALID_RESPONSE",
        userMessage = "서버 응답 형식이 올바르지 않아요.",
        cause = cause,
    )

    class Unknown(
        cause: Throwable? = null,
    ) : CommonError(
        errorCode = "COMMON_UNKNOWN",
        userMessage = "알 수 없는 문제가 생겼어요. 잠시 후 다시 시도해 주세요.",
        cause = cause,
    )
}
