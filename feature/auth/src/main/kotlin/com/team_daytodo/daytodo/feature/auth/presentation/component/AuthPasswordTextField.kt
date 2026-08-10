package com.team_daytodo.daytodo.feature.auth.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.auth.R
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextFieldColors
import com.team_daytodo.daytodo.uikit.component.dayTodoPressedScaleClickable

@Composable
fun AuthPasswordTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: DayTodoAuthTextFieldColors = DayTodoAuthTextFieldColors.default(),
) {
    DayTodoAuthTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        colors = colors,
        contentPadding = PasswordFieldContentPadding,
        trailingContent = {
            Box(
                modifier = Modifier
                    .size(width = 24.dp, height = 24.dp)
                    .dayTodoPressedScaleClickable(
                        role = Role.Button,
                        onClick = onPasswordVisibilityClick,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_visibility),
                    contentDescription = if (passwordVisible) {
                        "비밀번호 숨기기"
                    } else {
                        "비밀번호 보기"
                    },
                    modifier = Modifier.size(width = 22.dp, height = 15.dp),
                    tint = Color.Unspecified,
                )
            }
        },
    )
}

private val PasswordFieldContentPadding = PaddingValues(
    start = 20.dp,
    top = 18.dp,
    end = 13.dp,
    bottom = 18.dp,
)
