package com.team_daytodo.daytodo.uikit.theme

import android.R.attr.fontFamily
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class DayTodoTypography(
    val headlineLarge: TextStyle,
    val headlineSmall: TextStyle,

    val title1: TextStyle,
    val title2: TextStyle,

    val label1: TextStyle,
    val label2: TextStyle,
    val label3: TextStyle,

    val body1: TextStyle,
    val body2: TextStyle,

    val caption1: TextStyle,
    val caption2: TextStyle,
)

private fun dayTodoTextStyle(
    fontFamily: FontFamily,
    fontWeight: FontWeight,
    fontSize: Int,
    lineHeightRatio: Float,
): TextStyle =
    TextStyle(
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize.sp,
        lineHeight = (fontSize * lineHeightRatio).sp,
    )

internal val dayTodoTypography =
    DayTodoTypography(
        headlineLarge = dayTodoTextStyle(
            fontFamily = suiteFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24,
            lineHeightRatio = 1.30f
        ),
        headlineSmall = dayTodoTextStyle(
            fontFamily = suiteFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16,
            lineHeightRatio = 1.30f
        ),
        title1 = dayTodoTextStyle(
            fontFamily = suiteFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20,
            lineHeightRatio = 1.30f
        ),
        title2 = dayTodoTextStyle(
            fontFamily = suiteFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16,
            lineHeightRatio = 1.30f
        ),
        label1 = dayTodoTextStyle(
            fontFamily = suiteFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20,
            lineHeightRatio = 1.30f
        ),
        label2 = dayTodoTextStyle(
            fontFamily = suiteFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16,
            lineHeightRatio = 1.30f
        ),
        label3 = dayTodoTextStyle(
            fontFamily = suiteFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14,
            lineHeightRatio = 1.30f
        ),
        body1 = dayTodoTextStyle(
            fontFamily = suiteFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20,
            lineHeightRatio = 1.40f
        ),
        body2 = dayTodoTextStyle(
            fontFamily = pretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12,
            lineHeightRatio = 1.50f
        ),
        caption1 =dayTodoTextStyle(
            fontFamily = pretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14,
            lineHeightRatio = 1.30f
        ),
        caption2 = dayTodoTextStyle(
            fontFamily = pretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12,
            lineHeightRatio = 1.30f
        )
    )

internal val localDayTodoTypography = staticCompositionLocalOf {
    dayTodoTypography
}