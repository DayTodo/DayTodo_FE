package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
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
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.passwordchange.AuthIntroSection
import com.team_daytodo.daytodo.feature.mypage.component.passwordchange.AuthPasswordTextField
import com.team_daytodo.daytodo.feature.mypage.state.PasswordChangeUiState
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextFieldColors
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButtonState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import com.team_daytodo.daytodo.uikit.theme.pretendardFontFamily

@Composable
fun PasswordChangeScreen(
    uiState: PasswordChangeUiState,
    onBackClick: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onNewPasswordConfirmChange: (String) -> Unit,
    onCurrentPasswordVisibilityClick: () -> Unit,
    onNewPasswordVisibilityClick: () -> Unit,
    onNewPasswordConfirmVisibilityClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val inputColors = DayTodoAuthTextFieldColors.filledWhenUnfocused()
    val newPasswordMatchBorderColor = when {
        !uiState.hasPasswordComparison -> null
        uiState.isNewPasswordMatched -> PasswordMatchedBorderColor
        else -> PasswordMismatchedBorderColor
    }
    val newPasswordInputColors = newPasswordMatchBorderColor?.let { borderColor ->
        inputColors.copy(
            focusedBorder = borderColor,
            unfocusedBorder = borderColor,
        )
    } ?: inputColors
    val resetButtonState = when {
        uiState.isLoading -> DayTodoNextStepButtonState.Loading
        uiState.canSubmit -> DayTodoNextStepButtonState.Enabled
        else -> DayTodoNextStepButtonState.Incomplete
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = "비밀번호 변경", onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 132.dp),
            ) {
                item {
                    AuthIntroSection(
                        title = "안전한 비밀번호로\n변경해 주세요",
                        description = "※ 영문과 숫자를 조합해 8자 이상 입력해주세요",
                        modifier = Modifier
                            .padding(horizontal = ScreenHorizontalPadding)
                            .padding(top = 44.dp),
                    )
                }
                item { Spacer(modifier = Modifier.height(44.dp)) }
                item {
                    AuthPasswordTextField(
                        label = "현재 비밀번호",
                        value = uiState.currentPassword,
                        onValueChange = onCurrentPasswordChange,
                        passwordVisible = uiState.isCurrentPasswordVisible,
                        onPasswordVisibilityClick = onCurrentPasswordVisibilityClick,
                        modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                        placeholder = "현재 비밀번호 입력",
                        imeAction = ImeAction.Next,
                        colors = inputColors,
                    )
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                item {
                    AuthPasswordTextField(
                        label = "새 비밀번호",
                        value = uiState.newPassword,
                        onValueChange = onNewPasswordChange,
                        passwordVisible = uiState.isNewPasswordVisible,
                        onPasswordVisibilityClick = onNewPasswordVisibilityClick,
                        modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                        placeholder = "영문, 숫자 포함 8자 이상",
                        imeAction = ImeAction.Next,
                        colors = newPasswordInputColors,
                    )
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                item {
                    AuthPasswordTextField(
                        label = "새 비밀번호 확인",
                        value = uiState.newPasswordConfirm,
                        onValueChange = onNewPasswordConfirmChange,
                        passwordVisible = uiState.isNewPasswordConfirmVisible,
                        onPasswordVisibilityClick = onNewPasswordConfirmVisibilityClick,
                        modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                        placeholder = "영문, 숫자 포함 8자 이상",
                        colors = newPasswordInputColors,
                    )
                    if (uiState.isNewPasswordMismatched) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "새 비밀번호와 일치하지 않습니다",
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
                state = resetButtonState,
                onClick = onResetClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = ScreenHorizontalPadding)
                    .padding(bottom = BottomButtonPadding),
            )
        }
    }
}

private val PasswordMatchedBorderColor = Color(0xFF509B00)
private val PasswordMismatchedBorderColor = Color(0xFFFF607E)

@Preview
@Composable
private fun PasswordChangeScreenPreview() {
    PasswordChangeScreen(
        uiState = PasswordChangeUiState(),
        onBackClick = {},
        onCurrentPasswordChange = {},
        onNewPasswordChange = {},
        onNewPasswordConfirmChange = {},
        onCurrentPasswordVisibilityClick = {},
        onNewPasswordVisibilityClick = {},
        onNewPasswordConfirmVisibilityClick = {},
        onResetClick = {},
    )
}
