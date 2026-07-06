package com.team_daytodo.daytodo.core.model

sealed interface DataResult<out T> {
    data class Success<T>(val value: T) : DataResult<T>

    data class Error(
        val cause: Throwable,
        val message: String? = cause.message,
    ) : DataResult<Nothing>
}
