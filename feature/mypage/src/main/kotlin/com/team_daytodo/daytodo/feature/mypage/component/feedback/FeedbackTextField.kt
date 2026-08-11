package com.team_daytodo.daytodo.feature.mypage.component.feedback

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val FieldMinHeight = 345.dp
private val FieldShape = RoundedCornerShape(20.dp)

@Composable
fun FeedbackTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = DayTodoTheme.typography.body2.copy(
        color = DayTodoTheme.colors.textPrimary,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = FieldMinHeight)
            .clip(FieldShape)
            .border(1.dp, DayTodoTheme.colors.divider, FieldShape)
            .padding(16.dp),
        textStyle = textStyle,
        cursorBrush = SolidColor(DayTodoTheme.colors.brandPrimary),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = DayTodoTheme.colors.textTertiary,
                    )
                }
                innerTextField()
            }
        },
    )
}
