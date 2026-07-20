package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.course.presentation.FieldBorderColor
import com.team_daytodo.daytodo.feature.course.presentation.FieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.PlaceholderColor
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun CourseSelectionField(
    text: String,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, FieldBorderColor, RoundedCornerShape(12.dp))
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = text.ifBlank { placeholder },
            style = DayTodoTheme.typography.label2.copy(letterSpacing = 0.sp),
            color = if (text.isBlank()) PlaceholderColor else FieldContentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}