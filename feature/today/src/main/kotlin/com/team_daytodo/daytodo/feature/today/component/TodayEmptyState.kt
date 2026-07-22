package com.team_daytodo.daytodo.feature.today.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.today.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun TodayEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_emptystate),
            contentDescription = null,
            modifier = Modifier.size(150.dp),
            tint = Color.Unspecified,
        )
        Text(
            text = "현재 진행중인 코스가 없어요",
            style = DayTodoTheme.typography.label2,
            color = DayTodoTheme.colors.textSecondary,
        )
    }
}
