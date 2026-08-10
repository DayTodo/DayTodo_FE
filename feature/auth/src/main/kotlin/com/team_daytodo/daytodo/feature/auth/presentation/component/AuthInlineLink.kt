package com.team_daytodo.daytodo.feature.auth.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.uikit.component.dayTodoPressedScaleClickable
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun AuthInlineLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.dayTodoPressedScaleClickable(
            role = Role.Button,
            onClick = onClick,
        ),
        style = DayTodoTheme.typography.caption1.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        ),
        color = DayTodoTheme.colors.brandPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}