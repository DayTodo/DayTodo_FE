package com.team_daytodo.daytodo.data.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import retrofit2.Response

internal fun Response<*>.parseServerError(json: Json): ServerError? {
    val rawBody = runCatching { errorBody()?.string() }.getOrNull()
        ?.takeIf(String::isNotBlank)
        ?: return null

    return runCatching {
        json.parseToJsonElement(rawBody).toServerError()
    }.getOrNull()
}

private fun JsonElement.toServerError(): ServerError {
    if (this is JsonPrimitive) {
        return ServerError(
            code = null,
            message = contentOrNull?.takeIf(String::isNotBlank),
        )
    }

    val root = jsonObject
    val objects = listOfNotNull(
        root,
        root.objectOrNull("data"),
        root.objectOrNull("result"),
        root.objectOrNull("error"),
    )

    return ServerError(
        code = objects.firstNotNullOfOrNull {
            it.textOrNull("code")
                ?: it.textOrNull("errorCode")
                ?: it.textOrNull("error_code")
        },
        message = objects.firstNotNullOfOrNull {
            it.textOrNull("message")
                ?: it.textOrNull("msg")
                ?: it.textOrNull("detail")
                ?: it.textOrNull("error")
        },
    )
}

private fun JsonObject.objectOrNull(key: String): JsonObject? =
    get(key) as? JsonObject

private fun JsonObject.textOrNull(key: String): String? =
    (get(key) as? JsonPrimitive)?.contentOrNull?.takeIf(String::isNotBlank)
