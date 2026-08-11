package com.team_daytodo.daytodo.data.network

import com.team_daytodo.daytodo.core.model.CommonError
import com.team_daytodo.daytodo.core.model.DayTodoException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response

internal fun Throwable.toNetworkError(json: Json): Throwable =
    when (this) {
        is DayTodoException -> this
        is SocketTimeoutException -> CommonError.Timeout(cause = this)
        is UnknownHostException,
        is ConnectException,
        is IOException,
        -> CommonError.NetworkUnavailable(cause = this)
        is SerializationException -> CommonError.InvalidResponse(cause = this)
        is HttpException -> response()?.toHttpError(json) ?: CommonError.Unknown(cause = this)
        else -> CommonError.Unknown(cause = this)
    }

internal fun Response<*>.toHttpError(json: Json): CommonError {
    val serverError = parseServerError(json)

    return when (code()) {
        400 -> CommonError.BadRequest(
            userMessage = serverError?.message,
            serverCode = serverError?.code,
        )
        401 -> CommonError.Unauthorized(
            userMessage = serverError?.message,
            serverCode = serverError?.code,
        )
        403 -> CommonError.Forbidden(
            userMessage = serverError?.message,
            serverCode = serverError?.code,
        )
        404 -> CommonError.NotFound(
            userMessage = serverError?.message,
            serverCode = serverError?.code,
        )
        409 -> CommonError.Conflict(
            userMessage = serverError?.message,
            serverCode = serverError?.code,
        )
        429 -> CommonError.TooManyRequests(
            userMessage = serverError?.message,
            serverCode = serverError?.code,
        )
        in 500..599 -> CommonError.Server(
            statusCode = code(),
            userMessage = serverError?.message,
            serverCode = serverError?.code,
        )
        else -> CommonError.Http(
            statusCode = code(),
            userMessage = serverError?.message,
            serverCode = serverError?.code,
        )
    }
}
