package com.team_daytodo.daytodo.uikit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun DayTodoIconBadgeButton(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
    enabled: Boolean = true,
    selected: Boolean = false,
    width: Dp = 60.dp,
    height: Dp = 51.dp,
    selectedBackgroundColor: Color = DayTodoTheme.colors.brandPrimary,
    unselectedBackgroundColor: Color = Color.White,
    selectedIconColor: Color = Color.White,
    unselectedIconColor: Color = DayTodoTheme.colors.iconDefault,
) {
    val backgroundColor = if (selected) selectedBackgroundColor else unselectedBackgroundColor
    val iconColor = if (selected) selectedIconColor else unselectedIconColor
    val borderColor = if (selected) selectedBackgroundColor else Color(0xFFC1C1C1)

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.size(28.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )
            if (badgeCount > 0) {
                CountBadge(
                    count = badgeCount,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp),
                )
            }
        }
    }
}

@Composable
private fun CountBadge(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(DayTodoTheme.colors.brandPrimary),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = count.coerceAtMost(99).toString(),
            style = DayTodoTheme.typography.caption2.copy(
                fontSize = 8.sp,
                lineHeight = 8.sp,
                letterSpacing = 0.sp,
            ),
            color = Color.White,
        )
    }
}
