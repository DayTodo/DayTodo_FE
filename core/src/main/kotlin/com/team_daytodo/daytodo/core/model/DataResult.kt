package com.team_daytodo.daytodo.core.model

sealed interface DataResult<out T> {
    data object Loading : DataResult<Nothing>

    data class Success<T>(
        val value: T,
        val isFromCache: Boolean = false,
    ) : DataResult<T>

    data class Error<T>(
        val error: DayTodoException,
        val cachedValue: T? = null,
    ) : DataResult<T>
}
