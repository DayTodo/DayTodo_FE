package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val ButtonHeight = 58.dp

private val ButtonShape = RoundedCornerShape(999.dp)

private const val DisabledAlpha = 0.48f

@Composable
fun ProfileSaveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight)
            .clip(ButtonShape)
            .background(
                if (enabled) {
                    DayTodoTheme.colors.brandPrimary
                } else {
                    DayTodoTheme.colors.brandPrimary.copy(alpha = DisabledAlpha)
                },
            )
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label1,
            color = DayTodoTheme.colors.textQuaternary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSaveButtonPreview() {
    DayTodoTheme {
        ProfileSaveButton(text = "저장하기", onClick = {})
    }
}
