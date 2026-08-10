package com.team_daytodo.daytodo.feature.onboarding.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingGuideUiModel
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun OnboardingGuideRow(
    guide: OnboardingGuideUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(DayTodoTheme.colors.badgeColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = guide.step.toString(),
                style = DayTodoTheme.typography.body2.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = guide.title,
                style = DayTodoTheme.typography.headlineSmall,
                color = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = guide.description,
                style = DayTodoTheme.typography.body2,
                color = DayTodoTheme.colors.textSecondary,
            )
        }
    }
}

@Preview
@Composable
fun PreviewOnboardingGuideRow() {
    OnboardingGuideRow(
        guide = OnboardingGuideUiModel
            (
            step = 1,
            title = "코스방을 만들어 초대하면\n함께 코스를 짜고 의견을 나눌 수 있어요",
            description = "코스방을 만들어 초대하면\n함께 코스를 짜고 의견을 나눌 수 있어요",
        )
    )
}