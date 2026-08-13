package com.team_daytodo.daytodo.data.network

import com.team_daytodo.daytodo.core.model.CommonError
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import retrofit2.Response

@Suppress("TooGenericExceptionCaught")
internal suspend fun <T> safeApiCall(
    json: Json,
    block: suspend () -> T,
): T =
    try {
        block()
    } catch (cause: CancellationException) {
        throw cause
    } catch (cause: Throwable) {
        throw cause.toNetworkError(json)
    }

@Suppress("TooGenericExceptionCaught")
internal suspend fun <T> safeApiResult(
    json: Json,
    block: suspend () -> T,
): Result<T> =
    try {
        Result.success(safeApiCall(json, block))
    } catch (cause: CancellationException) {
        throw cause
    } catch (cause: Throwable) {
        Result.failure(cause)
    }

internal fun <T> Response<T>.bodyOrThrow(
    json: Json,
    endpoint: String,
): T {
    if (!isSuccessful) throw toHttpError(json)

    return body() ?: throw CommonError.EmptyBody(endpoint = endpoint)
}

internal fun <T> Response<T>.bodyOrDefault(
    json: Json,
    defaultBody: T,
): T {
    if (!isSuccessful) throw toHttpError(json)

    return body() ?: defaultBody
}

internal fun Response<Unit>.successOrThrow(json: Json) {
    if (!isSuccessful) throw toHttpError(json)
}
