package com.team_daytodo.daytodo.feature.home.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.team_daytodo.daytodo.feature.home.R
import com.team_daytodo.daytodo.uikit.component.DayTodoEmptyStateCard

@Composable
internal fun HomeEmptyStateCard(
    message: String,
    modifier: Modifier = Modifier,
) {
    DayTodoEmptyStateCard(
        modifier = modifier,
        message = message,
        iconPainter = painterResource(id = R.drawable.ic_symbol),
    )
}

@Preview
@Composable
fun PreviewHomeEmptyStateCard() {
    HomeEmptyStateCard(
        message = "아직 생성된 방이 없어요.",
    )
}