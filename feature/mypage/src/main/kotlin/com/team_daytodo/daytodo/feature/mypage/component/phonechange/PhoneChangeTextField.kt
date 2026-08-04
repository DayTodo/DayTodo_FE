package com.team_daytodo.daytodo.feature.mypage.component.phonechange

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val FieldHeight = 54.dp
private val FieldShape = RoundedCornerShape(12.dp)

@Composable
fun PhoneChangeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val layoutDirection = LocalLayoutDirection.current
    val contentPadding = PaddingValues(12.dp)
    val textStyle = DayTodoTheme.typography.label2.copy(
        color = DayTodoTheme.colors.textPrimary,
        letterSpacing = 0.sp,
    )
    val brandPrimary = DayTodoTheme.colors.brandPrimary
    val selectionColors = remember(brandPrimary) {
        TextSelectionColors(
            handleColor = brandPrimary,
            backgroundColor = brandPrimary.copy(alpha = 0.22f),
        )
    }

    CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .height(FieldHeight)
                .clip(FieldShape)
                .border(1.dp, DayTodoTheme.colors.brandPrimary, FieldShape),
            singleLine = true,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            cursorBrush = SolidColor(DayTodoTheme.colors.brandPrimary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(
                                top = contentPadding.calculateTopPadding(),
                                bottom = contentPadding.calculateBottomPadding(),
                            ),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = textStyle,
                                color = DayTodoTheme.colors.textTertiary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        innerTextField()
                    }
                    if (trailingContent != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        trailingContent()
                    }
                }
            },
        )
    }
}
