package com.team_daytodo.daytodo.feature.mypage.component.phonechange

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val ButtonWidth = 100.dp
private val ButtonHeight = 31.dp
private val ButtonShape = RoundedCornerShape(8.dp)

@Composable
fun RequestVerificationCodeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(width = ButtonWidth, height = ButtonHeight)
            .clip(ButtonShape)
            .background(DayTodoTheme.colors.brandPrimary)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "인증코드 받기",
            style = DayTodoTheme.typography.label3,
            color = DayTodoTheme.colors.textQuaternary,
        )
    }
}
