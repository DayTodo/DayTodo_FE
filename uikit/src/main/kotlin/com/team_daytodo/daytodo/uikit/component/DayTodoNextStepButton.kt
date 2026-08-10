package com.team_daytodo.daytodo.uikit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

enum class DayTodoNextStepButtonState {
    Disabled,
    Incomplete,
    Enabled,
    Loading,
}

@Composable
fun DayTodoNextStepButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DayTodoNextStepButton(
        text = text,
        state = if (enabled) {
            DayTodoNextStepButtonState.Enabled
        } else {
            DayTodoNextStepButtonState.Disabled
        },
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
fun DayTodoNextStepButton(
    text: String,
    state: DayTodoNextStepButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when (state) {
        DayTodoNextStepButtonState.Enabled -> EnabledButtonColor
        DayTodoNextStepButtonState.Incomplete -> IncompleteButtonColor
        DayTodoNextStepButtonState.Disabled,
        DayTodoNextStepButtonState.Loading -> DisabledButtonColor
    }
    val clickable = state == DayTodoNextStepButtonState.Enabled ||
        state == DayTodoNextStepButtonState.Incomplete

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .background(backgroundColor)
            .clickable(
                enabled = clickable,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label1,
            color = Color.White,
        )
    }
}

private val DisabledButtonColor = Color(0xFFCBCAF5)
private val IncompleteButtonColor = Color(0xFFB1B0F7)
private val EnabledButtonColor = Color(0xFF8B8AF5)

@Preview
@Composable
fun PreviewNextStepButton() {
    DayTodoNextStepButton(
        text = "다음",
        state = DayTodoNextStepButtonState.Enabled,
        onClick = {},
    )
}
