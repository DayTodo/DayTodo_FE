package com.team_daytodo.daytodo.uikit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object DayTodoTheme {
    val colors: DayTodoColors
        @Composable
        @ReadOnlyComposable
        get() = localDayTodoColors.current

    val typography: DayTodoTypography
        @Composable
        @ReadOnlyComposable
        get() = localDayTodoTypography.current
}

@Suppress("UnusedParameter")
@Composable
fun DayTodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = lightDayTodoColors
    val colorScheme = lightDayTodoColorScheme

    CompositionLocalProvider(
        localDayTodoColors provides colors,
        localDayTodoTypography provides dayTodoTypography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}
