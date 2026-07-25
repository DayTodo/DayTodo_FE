package com.team_daytodo.daytodo.feature.mypage.component.mypage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val RowStartPadding = 24.dp

private val ArrowEndPadding = 20.dp

private val LeadingIconSpacing = 16.dp

@Composable
fun MypageMenuRow(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = DayTodoTheme.colors.textPrimary,
    @DrawableRes leadingIconRes: Int? = null,
    trailingArrow: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                start = RowStartPadding,
                end = if (trailingArrow) ArrowEndPadding else 0.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIconRes != null) {
            Icon(
                painter = painterResource(id = leadingIconRes),
                contentDescription = null,
                tint = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.width(LeadingIconSpacing))
        }
        Text(
            text = text,
            style = DayTodoTheme.typography.label2,
            color = color,
            modifier = Modifier.weight(1f),
        )
        if (trailingArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = DayTodoTheme.colors.iconDefault,
            )
        }
    }
}
