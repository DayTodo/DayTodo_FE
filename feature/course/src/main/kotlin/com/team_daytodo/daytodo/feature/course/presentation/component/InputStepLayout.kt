package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.screenHorizontalPadding
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun InputStepLayout(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = screenHorizontalPadding)
            .padding(top = 42.dp),
    ) {
        Text(
            text = title,
            style = DayTodoTheme.typography.headlineLarge,
            color = fieldContentColor,
        )
        Spacer(modifier = Modifier.height(28.dp))
        content()
    }
}