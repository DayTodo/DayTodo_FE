package com.team_daytodo.daytodo.uikit.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Divider
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Material3 ColorScheme 매핑을 위함
 */
internal fun DayTodoColors.toMaterialColorScheme(): ColorScheme =
    lightColorScheme(
        primary = purple500,
        onPrimary = Color.Black,
        primaryContainer = purple100,
        onPrimaryContainer = purple900,

        secondary = yellow500,
        onSecondary = text1,
        secondaryContainer = yellow100,
        onSecondaryContainer = text1,

        tertiary = pink500,
        onTertiary = Color.Black,
        tertiaryContainer = pink100,
        onTertiaryContainer = pink900,

        background = background1,
        onBackground = text1,

        surface = background1,
        onSurface = text1,

        surfaceVariant = background2,
        onSurfaceVariant = text2,

        outline = divider,
        outlineVariant = divider,

        error = Color(0xFFE53935),
        onError = Color.White,
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
    )

internal val lightDayTodoColorScheme = lightDayTodoColors.toMaterialColorScheme()