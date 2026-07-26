package com.team_daytodo.daytodo.uikit.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TurnedIn
import androidx.compose.material.icons.filled.TurnedInNot
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun DayTodoBookmarkButton(
    saved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = if (saved) "저장 취소" else "저장",
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .dayTodoPressedScaleClickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 8.dp, vertical = 5.5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (saved) Icons.Filled.TurnedIn else Icons.Filled.TurnedInNot,
            contentDescription = contentDescription,
            tint = BookmarkColor,
            modifier = Modifier.size(width = 20.dp, height = 25.dp),
        )
    }
}

private val BookmarkColor = Color(0xFF8B8AF5)
