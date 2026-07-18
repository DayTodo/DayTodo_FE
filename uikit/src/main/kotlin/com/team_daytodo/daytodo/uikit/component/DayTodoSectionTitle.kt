package com.team_daytodo.daytodo.uikit.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun DayTodoSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = title,
        style = DayTodoTheme.typography.title1,
        color = DayTodoTheme.colors.textPrimary,
    )
}
