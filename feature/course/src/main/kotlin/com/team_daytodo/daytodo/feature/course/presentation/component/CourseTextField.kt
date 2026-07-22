package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldBorderColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.placeholderColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.progressColor
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun CourseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val textStyle = DayTodoTheme.typography.label2.copy(
        color = fieldContentColor,
        letterSpacing = 0.sp,
    )
    val selectionColors = remember {
        TextSelectionColors(
            handleColor = progressColor,
            backgroundColor = progressColor.copy(alpha = 0.22f),
        )
    }

    CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, fieldBorderColor, RoundedCornerShape(12.dp)),
            singleLine = true,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            cursorBrush = SolidColor(progressColor),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle,
                            color = placeholderColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}