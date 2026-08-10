package com.team_daytodo.daytodo.feature.auth.presentation

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.auth.model.FindPasswordUiState
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthInlineLink
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthIntroSection
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButtonState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun FindPasswordScreen(
    uiState: FindPasswordUiState,
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendCodeClick: () -> Unit,
    onFindIdClick: () -> Unit,
    onConfirmClick: () -> Unit,
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
                DayTodoHeaderSection(
                    title = "비밀번호 찾기",
                    onBackClick = onBackClick,
                    horizontalPadding = ScreenHorizontalPadding,
                )
            }
            item {
                AuthIntroSection(
                    title = "비밀번호를\n잊으셨나요?",
                    description = "가입하신 이메일로 인증코드를 보내드려요.\n인증 후 새 비밀번호를 설정할 수 있어요",
                    modifier = Modifier
                        .padding(horizontal = ScreenHorizontalPadding)
                        .padding(top = 44.dp),
                )
            }
            item { Spacer(modifier = Modifier.height(44.dp)) }
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
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 18.dp,
                        end = 12.dp,
                        bottom = 18.dp,
                    ),
                    trailingContent = {
                        VerificationCodeSendButton(
                            enabled = uiState.canSendCode,
                            loading = uiState.isSendingCode,
                            onClick = onSendCodeClick,
                        )
                    },
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                DayTodoAuthTextField(
                    label = "인증코드",
                    value = uiState.verificationCode,
                    onValueChange = onVerificationCodeChange,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    trailingContent = {
                        if (uiState.hasActiveCode) {
                            Text(
                                text = uiState.remainingTimeText,
                                style = DayTodoTheme.typography.caption1.copy(
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = 0.sp,
                                ),
                                color = TimerTextColor,
                                maxLines = 1,
                            )
                        }
                    },
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ScreenHorizontalPadding)
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    AuthInlineLink(
                        text = "아이디 찾기",
                        onClick = onFindIdClick,
                    )
                }
            }
        }

        DayTodoNextStepButton(
            text = "확인",
            state = if (uiState.canVerifyCode) {
                DayTodoNextStepButtonState.Enabled
            } else {
                DayTodoNextStepButtonState.Disabled
            },
            onClick = onConfirmClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = BottomButtonPadding),
        )
    }
}

@Composable
private fun VerificationCodeSendButton(
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (enabled) {
                    DayTodoTheme.colors.brandPrimary
                } else {
                    DayTodoTheme.colors.brandPrimary.copy(alpha = 0.48f)
                },
            )
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (loading) "발송 중" else "인증번호 받기",
            style = DayTodoTheme.typography.label3.copy(letterSpacing = 0.sp),
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private val TimerTextColor = Color(0xFFFF5555)

@Preview
@Composable
private fun FindPasswordScreenPreview() {
    FindPasswordScreen(
        uiState = FindPasswordUiState(remainingSeconds = 169),
        onBackClick = {},
        onEmailChange = {},
        onVerificationCodeChange = {},
        onSendCodeClick = {},
        onFindIdClick = {},
        onConfirmClick = {},
    )
}
