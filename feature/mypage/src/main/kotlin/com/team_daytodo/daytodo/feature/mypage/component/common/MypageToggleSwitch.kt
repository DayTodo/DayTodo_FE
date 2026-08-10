package com.team_daytodo.daytodo.feature.mypage.component.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val TrackWidth = 67.dp
private val TrackHeight = 29.5.dp
private val ThumbWidth = 41.dp
private val ThumbHeight = 25.dp

private val ThumbPadding = (TrackHeight - ThumbHeight) / 2

@Composable
fun MypageToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) TrackWidth - ThumbWidth - ThumbPadding else ThumbPadding,
        label = "MypageToggleSwitchThumbOffset",
    )

    Box(
        modifier = modifier
            .size(width = TrackWidth, height = TrackHeight)
            .clip(CircleShape)
            .background(
                if (checked) {
                    DayTodoTheme.colors.brandPrimary
                } else {
                    DayTodoTheme.colors.iconDisabled
                },
            )
            .toggleable(
                value = checked,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(width = ThumbWidth, height = ThumbHeight)
                .clip(CircleShape)
                .background(DayTodoTheme.colors.iconOnColor),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MypageToggleSwitchPreview() {
    DayTodoTheme {
        MypageToggleSwitch(checked = true, onCheckedChange = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MypageToggleSwitchOffPreview() {
    DayTodoTheme {
        MypageToggleSwitch(checked = false, onCheckedChange = {})
    }
}
