package com.team_daytodo.daytodo.feature.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.home.R
import com.team_daytodo.daytodo.feature.home.model.HomeUiState
import com.team_daytodo.daytodo.feature.home.model.sampleHomeUiState
import com.team_daytodo.daytodo.uikit.R as UIKitR
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun HomeBanner(
    uiState: HomeUiState,
    onBookmarkClick: () -> Unit,
    onCalendarClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .background(DayTodoTheme.colors.brandPrimary)
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 9.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .width(78.dp)
                    .height(22.8.dp),
                painter = painterResource(id = UIKitR.drawable.ic_logo),
                contentDescription = "로고",
            )
            LocationText(text = uiState.interestLocation)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "안녕하세요, ${uiState.username}님",
                    style = DayTodoTheme.typography.headlineSmall,
                    color = DayTodoTheme.colors.textQuaternary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = uiState.scheduleMessage(),
                    style = DayTodoTheme.typography.headlineLarge,
                    color = DayTodoTheme.colors.textQuaternary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                HomeHeaderActionButton(
                    iconResId = R.drawable.ic_bookmark,
                    contentDescription = "저장한 항목 보기",
                    onClick = onBookmarkClick,
                )
                HomeHeaderActionButton(
                    iconResId = R.drawable.ic_calendar,
                    contentDescription = "캘린더 보기",
                    onClick = onCalendarClick,
                )
            }
        }
    }
}

@Composable
private fun LocationText(text: String) {
    val lineColor = Color(0xFFE4E4E4)

    Text(
        modifier = Modifier.drawBehind {
            drawLine(
                color = lineColor,
                start = Offset(x = 0f, y = size.height),
                end = Offset(x = size.width, y = size.height),
                strokeWidth = 0.5.dp.toPx(),
            )
        },
        text = text,
        style = DayTodoTheme.typography.label3,
        color = lineColor,
    )
}

@Composable
private fun HomeHeaderActionButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.size(32.dp),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            tint = DayTodoTheme.colors.iconOnColor,
        )
    }
}

private fun HomeUiState.scheduleMessage(): String =
    when {
        todayCourse != null -> "오늘 ${todayCourse.relationship.scheduleTargetLabel}의 일정이 있어요!"
        upcomingCourse != null -> "곧 다가오는 일정이 있어요!"
        else -> "오늘은 아무 일정이 없어요."
    }

@Preview
@Composable
fun PreviewHomeBanner() {
    HomeBanner(
        uiState = sampleHomeUiState(),
        onBookmarkClick = {},
        onCalendarClick = {},
    )
}
