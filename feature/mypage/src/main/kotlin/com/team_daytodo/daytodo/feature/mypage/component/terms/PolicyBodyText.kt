package com.team_daytodo.daytodo.feature.mypage.component.terms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun PolicyBodyText(
    body: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = body,
        style = DayTodoTheme.typography.body3,
        color = DayTodoTheme.colors.textSecondary,
        modifier = modifier,
    )
}
