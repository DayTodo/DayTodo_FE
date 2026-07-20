package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.course.presentation.FieldBorderColor
import com.team_daytodo.daytodo.feature.course.presentation.FieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.PlaceholderColor
import com.team_daytodo.daytodo.feature.course.presentation.ProgressColor
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import java.text.DecimalFormat

@Composable
fun BudgetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val formattedValue = formatBudgetDigits(value)
    val textStyle = DayTodoTheme.typography.label2.copy(
        color = FieldContentColor,
        letterSpacing = 0.sp,
    )
    val selectionColors = remember {
        TextSelectionColors(
            handleColor = ProgressColor,
            backgroundColor = ProgressColor.copy(alpha = 0.22f),
        )
    }

    CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
        BasicTextField(
            value = formattedValue,
            onValueChange = onValueChange,
            modifier = modifier
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, FieldBorderColor, RoundedCornerShape(12.dp)),
            singleLine = true,
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(ProgressColor),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (formattedValue.isEmpty()) {
                            Text(
                                text = "0",
                                style = textStyle,
                                color = PlaceholderColor,
                            )
                        }
                        innerTextField()
                    }
                    Text(
                        text = "원",
                        style = textStyle,
                        color = if (formattedValue.isEmpty()) PlaceholderColor else FieldContentColor,
                    )
                }
            },
        )
    }
}

private fun formatBudgetDigits(value: String): String {
    if (value.isBlank()) return ""
    val number = value.toLongOrNull() ?: return value
    return DecimalFormat("#,###").format(number)
}