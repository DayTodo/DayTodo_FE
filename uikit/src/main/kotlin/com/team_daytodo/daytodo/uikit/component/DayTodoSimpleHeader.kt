package com.team_daytodo.daytodo.uikit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun DayTodoSimpleHeader(
    title: String,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 10.dp,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(DayTodoTheme.colors.backgroundDefault)
                .padding(vertical = verticalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title,
                style = DayTodoTheme.typography.headlineSmall,
                color = DayTodoTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = DayTodoTheme.colors.divider,
        )
    }
}