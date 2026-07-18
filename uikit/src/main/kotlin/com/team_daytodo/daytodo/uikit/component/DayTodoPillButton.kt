package com.team_daytodo.daytodo.uikit.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun DayTodoPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(10.dp),
) {
    Surface(
        modifier = modifier.clickable(
            role = Role.Button,
            onClick = onClick,
        ),
        shape = RoundedCornerShape(64.dp),
        color = DayTodoTheme.colors.brandPrimary,
    ) {
        Text(
            modifier = Modifier.padding(contentPadding),
            text = text,
            style = DayTodoTheme.typography.label3,
            color = DayTodoTheme.colors.textQuaternary,
        )
    }
}
