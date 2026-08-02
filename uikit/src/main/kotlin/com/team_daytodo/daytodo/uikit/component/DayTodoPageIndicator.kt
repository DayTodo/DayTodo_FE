package com.team_daytodo.daytodo.uikit.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun DayTodoPageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = DayTodoTheme.colors.brandPrimary,
    inactiveColor: Color = DayTodoTheme.colors.divider,
    activeWidth: Dp = 18.dp,
    inactiveSize: Dp = 7.dp,
    spacing: Dp = 7.dp,
) {
    if (pageCount <= 0) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val selected = index == currentPage.coerceIn(0, pageCount - 1)
            val width by animateDpAsState(
                targetValue = if (selected) activeWidth else inactiveSize,
                animationSpec = tween(durationMillis = IndicatorAnimationMillis),
                label = "daytodo-page-indicator-width",
            )
            val color by animateColorAsState(
                targetValue = if (selected) activeColor else inactiveColor,
                animationSpec = tween(durationMillis = IndicatorAnimationMillis),
                label = "daytodo-page-indicator-color",
            )

            Box(
                modifier = Modifier
                    .width(width)
                    .height(inactiveSize)
                    .clip(RoundedCornerShape(999.dp))
                    .background(color),
            )
        }
    }
}

private const val IndicatorAnimationMillis = 220
