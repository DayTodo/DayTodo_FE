package com.team_daytodo.daytodo.uikit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import com.team_daytodo.daytodo.uikit.theme.pretendardFontFamily

@Composable
fun DayTodoAuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: DayTodoAuthTextFieldColors = DayTodoAuthTextFieldColors.default(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
    textStyle: TextStyle = DayTodoTheme.typography.caption1,
    labelStyle: TextStyle = DayTodoTheme.typography.label2,
    labelSpacing: Dp = 8.dp,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)
    val layoutDirection = LocalLayoutDirection.current
    val backgroundColor = if (isFocused) colors.focusedBackground else colors.unfocusedBackground
    val borderColor = if (isFocused) colors.focusedBorder else colors.unfocusedBorder
    val borderWidth = if (borderColor == Color.Transparent) 0.dp else 1.dp
    val inputTextStyle = textStyle.copy(
        color = DayTodoTheme.colors.textPrimary,
        letterSpacing = 0.sp,
    )
    val selectionColors = remember {
        TextSelectionColors(
            handleColor = colors.cursorColor,
            backgroundColor = colors.cursorColor.copy(alpha = 0.22f),
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = labelStyle,
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(labelSpacing))
        CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(shape)
                    .background(backgroundColor)
                    .border(borderWidth, borderColor, shape)
                    .onFocusChanged { isFocused = it.isFocused },
                singleLine = true,
                textStyle = inputTextStyle,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                cursorBrush = SolidColor(colors.cursorColor),
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
                            if (value.isEmpty() && placeholder.isNotEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = textStyle.copy(letterSpacing = 0.sp),
                                    color = DayTodoTheme.colors.textSecondary,
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
}

@Composable
fun DayTodoAuthCheckbox(
    checked: Boolean,
    label: String,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    labelSpacing: Dp = 10.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            role = Role.Checkbox,
            onClick = { onCheckedChange(!checked) },
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    if (checked) {
                        DayTodoTheme.colors.brandPrimary
                    } else {
                        CheckboxUncheckedColor
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.width(labelSpacing))
        Text(
            text = label,
            style = DayTodoTheme.typography.caption1.copy(
                fontFamily = pretendardFontFamily,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
            ),
            color = DayTodoTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

data class DayTodoAuthTextFieldColors(
    val focusedBackground: Color,
    val unfocusedBackground: Color,
    val focusedBorder: Color,
    val unfocusedBorder: Color,
    val cursorColor: Color,
) {
    companion object {
        @Composable
        fun default(): DayTodoAuthTextFieldColors =
            DayTodoAuthTextFieldColors(
                focusedBackground = FocusedInputBackground,
                unfocusedBackground = DayTodoTheme.colors.backgroundDefault,
                focusedBorder = Color.Transparent,
                unfocusedBorder = DayTodoTheme.colors.brandPrimary,
                cursorColor = DayTodoTheme.colors.brandPrimary,
            )

        @Composable
        fun filledWhenUnfocused(): DayTodoAuthTextFieldColors =
            DayTodoAuthTextFieldColors(
                focusedBackground = DayTodoTheme.colors.backgroundDefault,
                unfocusedBackground = UnfocusedFilledInputBackground,
                focusedBorder = DayTodoTheme.colors.brandPrimary,
                unfocusedBorder = Color.Transparent,
                cursorColor = DayTodoTheme.colors.brandPrimary,
            )
    }
}

private val CheckboxUncheckedColor = Color(0xFFE6E6E6)
private val FocusedInputBackground = Color(0xFFEEEEFD)
private val UnfocusedFilledInputBackground = Color(0xFFF2F2F2)

@Preview
@Composable
private fun DayTodoAuthComponentsPreview() {
    Column(modifier = Modifier.background(Color.White).padding(20.dp)) {
        DayTodoSimpleHeader(title = "로그인")
        Spacer(modifier = Modifier.height(20.dp))
        DayTodoAuthTextField(
            label = "아이디(이메일)",
            value = "",
            onValueChange = {},
            placeholder = "example@daytodo.com",
        )
        Spacer(modifier = Modifier.height(20.dp))
        DayTodoAuthCheckbox(
            checked = true,
            label = "로그인 상태 유지",
            onCheckedChange = {},
        )
        Spacer(modifier = Modifier.height(20.dp))
        DayTodoNextStepButton(
            text = "로그인",
            enabled = true,
            onClick = {},
        )
    }
}
