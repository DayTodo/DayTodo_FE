package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

internal val ProfileFieldShape = RoundedCornerShape(12.dp)

private val DefaultFieldHeight = 41.dp

@Composable
fun ProfileInfoField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
    height: Dp = DefaultFieldHeight,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = DayTodoTheme.typography.title2,
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(ProfileFieldShape)
                .then(
                    if (filled) {
                        Modifier.background(DayTodoTheme.colors.backgroundSecondary)
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = DayTodoTheme.colors.brandPrimary,
                            shape = ProfileFieldShape,
                        )
                    },
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = value,
                style = DayTodoTheme.typography.caption1,
                color = DayTodoTheme.colors.textPrimary,
            )
        }
    }
}
