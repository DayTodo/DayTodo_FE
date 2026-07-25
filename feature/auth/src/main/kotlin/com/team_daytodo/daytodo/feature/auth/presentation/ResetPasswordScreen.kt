package com.team_daytodo.daytodo.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.auth.model.ResetPasswordUiState
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthIntroSection
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthPasswordTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextFieldColors
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.component.DayTodoSimpleHeader
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import com.team_daytodo.daytodo.uikit.theme.pretendardFontFamily

@Composable
fun ResetPasswordScreen(
    uiState: ResetPasswordUiState,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    onPasswordConfirmVisibilityClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val inputColors = DayTodoAuthTextFieldColors.filledWhenUnfocused()
    val passwordMatchBorderColor = when {
        !uiState.hasPasswordComparison -> null
        uiState.isPasswordMatched -> PasswordMatchedBorderColor
        else -> PasswordMismatchedBorderColor
    }
    val passwordInputColors = passwordMatchBorderColor?.let { borderColor ->
        inputColors.copy(
            focusedBorder = borderColor,
            unfocusedBorder = borderColor,
        )
    } ?: inputColors

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault)
            .statusBarsPadding(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 132.dp),
        ) {
            item {
                DayTodoSimpleHeader(title = "비밀번호 변경")
            }
            item {
                AuthIntroSection(
                    title = "안전한 비밀번호로\n변경해주세요",
                    description = "※ 영문과 숫자를 조합해 8자 이상 입력해주세요",
                    modifier = Modifier
                        .padding(horizontal = ScreenHorizontalPadding)
                        .padding(top = 44.dp),
                )
            }
            item { Spacer(modifier = Modifier.height(44.dp)) }
            item {
                AuthPasswordTextField(
                    label = "새 비밀번호",
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    passwordVisible = uiState.isPasswordVisible,
                    onPasswordVisibilityClick = onPasswordVisibilityClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    placeholder = "영문, 숫자 포함 8자 이상",
                    imeAction = ImeAction.Next,
                    colors = passwordInputColors,
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                AuthPasswordTextField(
                    label = "새 비밀번호 확인",
                    value = uiState.passwordConfirm,
                    onValueChange = onPasswordConfirmChange,
                    passwordVisible = uiState.isPasswordConfirmVisible,
                    onPasswordVisibilityClick = onPasswordConfirmVisibilityClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    placeholder = "영문, 숫자 포함 8자 이상",
                    colors = passwordInputColors,
                )
                if (uiState.isPasswordMismatched) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "새 비밀번호와 일치하지 않습니다.",
                        modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                        style = DayTodoTheme.typography.caption2.copy(
                            fontFamily = pretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            lineHeight = 15.6.sp,
                            letterSpacing = (-0.24).sp,
                        ),
                        color = PasswordMismatchedBorderColor,
                    )
                }
            }
        }

        DayTodoNextStepButton(
            text = "변경하기",
            enabled = uiState.canSubmit,
            allowDisabledClick = true,
            onClick = onResetClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = BottomButtonPadding),
        )
    }
}

private val PasswordMatchedBorderColor = Color(0xFF509B00)
private val PasswordMismatchedBorderColor = Color(0xFFFF607E)

@Preview
@Composable
private fun ResetPasswordScreenPreview() {
    ResetPasswordScreen(
        uiState = ResetPasswordUiState(),
        onPasswordChange = {},
        onPasswordConfirmChange = {},
        onPasswordVisibilityClick = {},
        onPasswordConfirmVisibilityClick = {},
        onResetClick = {},
    )
}
