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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.auth.model.SignupUiState
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthIntroSection
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthPasswordTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthCheckbox
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextFieldColors
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun SignupScreen(
    uiState: SignupUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onTermsAgreementChange: (Boolean) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    onPasswordConfirmVisibilityClick: () -> Unit,
    onSignupClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val inputColors = DayTodoAuthTextFieldColors.filledWhenUnfocused()

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
                DayTodoHeaderSection(
                    title = "회원가입",
                    onBackClick = onBackClick,
                    horizontalPadding = ScreenHorizontalPadding,
                )
            }
            item {
                AuthIntroSection(
                    title = "함께해요, 데이투두",
                    description = "몇 가지 정보만 입력하면 바로 시작돼요",
                    modifier = Modifier
                        .padding(horizontal = ScreenHorizontalPadding)
                        .padding(top = 44.dp),
                )
            }
            item { Spacer(modifier = Modifier.height(46.dp)) }
            item {
                DayTodoAuthTextField(
                    label = "아이디(이메일)",
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    placeholder = "example@daytodo.com",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    colors = inputColors,
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                AuthPasswordTextField(
                    label = "비밀번호",
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    passwordVisible = uiState.isPasswordVisible,
                    onPasswordVisibilityClick = onPasswordVisibilityClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    placeholder = "영문, 숫자 포함 8자 이상",
                    imeAction = ImeAction.Next,
                    colors = inputColors,
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                AuthPasswordTextField(
                    label = "비밀번호 확인",
                    value = uiState.passwordConfirm,
                    onValueChange = onPasswordConfirmChange,
                    passwordVisible = uiState.isPasswordConfirmVisible,
                    onPasswordVisibilityClick = onPasswordConfirmVisibilityClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    placeholder = "영문, 숫자 포함 8자 이상",
                    colors = inputColors,
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                DayTodoAuthCheckbox(
                    checked = uiState.agreedToTerms,
                    label = "이용약관 및 개인정보처리방침에 동의합니다.",
                    onCheckedChange = onTermsAgreementChange,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                )
            }
        }

        DayTodoNextStepButton(
            text = "가입하기",
            enabled = uiState.canSubmit,
            allowDisabledClick = true,
            onClick = onSignupClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = BottomButtonPadding),
        )
    }
}

@Preview
@Composable
private fun SignupScreenPreview() {
    SignupScreen(
        uiState = SignupUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onPasswordConfirmChange = {},
        onTermsAgreementChange = {},
        onPasswordVisibilityClick = {},
        onPasswordConfirmVisibilityClick = {},
        onSignupClick = {},
        onBackClick = {},
    )
}
