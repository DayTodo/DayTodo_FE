package com.team_daytodo.daytodo.feature.mypage.component.phonechange

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val ButtonWidth = 100.dp
private val ButtonHeight = 31.dp
private val ButtonShape = RoundedCornerShape(67.dp)
private val ResendBorderColor = Color(0xFFE8E7E6)
private const val DisabledAlpha = 0.48f

@Composable
fun RequestVerificationCodeButton(
    hasRequestedCode: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val baseBackgroundColor = if (hasRequestedCode) {
        DayTodoTheme.colors.backgroundDefault
    } else {
        DayTodoTheme.colors.brandPrimary
    }
    val backgroundColor = if (enabled) baseBackgroundColor else baseBackgroundColor.copy(alpha = DisabledAlpha)
    val borderModifier = if (hasRequestedCode) {
        Modifier.border(1.dp, ResendBorderColor, ButtonShape)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(width = ButtonWidth, height = ButtonHeight)
            .clip(ButtonShape)
            .then(borderModifier)
            .background(backgroundColor)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (hasRequestedCode) "다시 코드 받기" else "인증코드 받기",
            style = DayTodoTheme.typography.label3,
            color = if (hasRequestedCode) {
                DayTodoTheme.colors.textPrimary
            } else {
                DayTodoTheme.colors.textQuaternary
            },
        )
    }
}
