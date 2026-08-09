package com.team_daytodo.daytodo.feature.mypage.component.passwordchange

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun AuthIntroSection(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = DayTodoTheme.typography.headlineLarge.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = DayTodoTheme.typography.body3.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textSecondary,
        )
    }
}
