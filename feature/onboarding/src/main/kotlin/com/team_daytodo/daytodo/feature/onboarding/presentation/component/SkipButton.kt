package com.team_daytodo.daytodo.feature.onboarding.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun SkipButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "건너뛰기",
        modifier = modifier.clickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
        ),
        style = DayTodoTheme.typography.label3,
        color = DayTodoTheme.colors.textSecondary,
    )
}