package com.team_daytodo.daytodo.feature.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun CourseScreen(
    mode: CourseScreenMode,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = mode.title,
                style = DayTodoTheme.typography.title1,
                color = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mode.description,
                style = DayTodoTheme.typography.caption1,
                color = DayTodoTheme.colors.textSecondary,
            )
        }
    }
}
