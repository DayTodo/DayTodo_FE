package com.team_daytodo.daytodo.uikit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class DayTodoColors(
    val brandPrimary: Color,
    val brandPrimaryStrong: Color,
    val brandPrimaryEmphasis: Color,
    val brandSecondary: Color,
    val brandSecondaryStrong: Color,
    val brandSecondaryEmphasis: Color,
    val brandTertiary: Color,
    val brandTertiaryStrong: Color,
    val brandTertiaryEmphasis: Color,

    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textQuaternary: Color,

    val iconDefault: Color,
    val iconSelected: Color,
    val iconDisabled: Color,
    val iconOnColor: Color,

    val backgroundDefault: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,

    val divider: Color,
)

internal val lightDayTodoColors = DayTodoColors(
    brandPrimary = purple500,
    brandPrimaryStrong = purple600,
    brandPrimaryEmphasis = purple700,
    brandSecondary = yellow500,
    brandSecondaryStrong = yellow600,
    brandSecondaryEmphasis = yellow700,
    brandTertiary = pink500,
    brandTertiaryStrong = pink600,
    brandTertiaryEmphasis = pink700,

    textPrimary = text1,
    textSecondary = text2,
    textTertiary = text3,
    textQuaternary = text4,

    iconDefault = icon,
    iconSelected = iconColor,
    iconDisabled = iconDisabled,
    iconOnColor = iconW,

    backgroundDefault = background1,
    backgroundSecondary = background2,
    backgroundTertiary = background3,

    divider = divider,
)

internal val localDayTodoColors = staticCompositionLocalOf {
    lightDayTodoColors
}
