package com.team_daytodo.daytodo.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun OnboardingTextBlock(
    headline: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = headline,
            style = DayTodoTheme.typography.headlineLarge,
            color = DayTodoTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Text(
            text = description,
            style = DayTodoTheme.typography.body2,
            color = DayTodoTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}