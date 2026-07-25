package com.team_daytodo.daytodo.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.auth.R
import com.team_daytodo.daytodo.feature.auth.model.LoginUiState
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthInlineLink
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthIntroSection
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthPasswordTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthCheckbox
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.component.DayTodoSimpleHeader
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onKeepLoggedInChange: (Boolean) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    onSignupClick: () -> Unit,
    onFindPasswordClick: () -> Unit,
    onNaverLoginClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                DayTodoSimpleHeader(title = "로그인")
            }
            item {
                AuthIntroSection(
                    title = "만나서 반가워요",
                    description = "계정에 로그인하고 코스를 이어가요",
                    modifier = Modifier
                        .padding(horizontal = ScreenHorizontalPadding)
                        .padding(top = 44.dp),
                )
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }
            item {
                DayTodoAuthTextField(
                    label = "아이디(이메일)",
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                AuthPasswordTextField(
                    label = "비밀번호",
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    passwordVisible = uiState.isPasswordVisible,
                    onPasswordVisibilityClick = onPasswordVisibilityClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    keyboardActions = KeyboardActions(onDone = { onLoginClick() }),
                )
            }
            item { Spacer(modifier = Modifier.height(14.dp)) }
            item {
                LoginOptionsRow(
                    keepLoggedIn = uiState.keepLoggedIn,
                    onKeepLoggedInChange = onKeepLoggedInChange,
                    onSignupClick = onSignupClick,
                    onFindPasswordClick = onFindPasswordClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                )
            }
            item { Spacer(modifier = Modifier.height(58.dp)) }
            item {
                NaverLoginButton(
                    enabled = !uiState.isLoading,
                    onClick = onNaverLoginClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                )
            }
        }

        DayTodoNextStepButton(
            text = "로그인",
            enabled = uiState.canSubmit,
            onClick = onLoginClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = BottomButtonPadding),
        )
    }
}

@Composable
private fun LoginOptionsRow(
    keepLoggedIn: Boolean,
    onKeepLoggedInChange: (Boolean) -> Unit,
    onSignupClick: () -> Unit,
    onFindPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DayTodoAuthCheckbox(
            checked = keepLoggedIn,
            label = "로그인 상태 유지",
            onCheckedChange = onKeepLoggedInChange,
            labelSpacing = 12.dp,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            AuthInlineLink(
                text = "회원가입하기",
                onClick = onSignupClick,
            )
            Text(
                text = "|",
                modifier = Modifier.padding(horizontal = 4.dp),
                style = DayTodoTheme.typography.caption1.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.sp,
                ),
                color = DayTodoTheme.colors.brandPrimary,
            )
            AuthInlineLink(
                text = "비밀번호 찾기",
                onClick = onFindPasswordClick,
            )
        }
    }
}

@Composable
private fun NaverLoginButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NaverButtonColor)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_naver),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "네이버로 시작하기",
            style = DayTodoTheme.typography.label2.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private val NaverButtonColor = Color(0xFF00CC00)

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        uiState = LoginUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onKeepLoggedInChange = {},
        onPasswordVisibilityClick = {},
        onSignupClick = {},
        onFindPasswordClick = {},
        onNaverLoginClick = {},
        onLoginClick = {},
    )
}
