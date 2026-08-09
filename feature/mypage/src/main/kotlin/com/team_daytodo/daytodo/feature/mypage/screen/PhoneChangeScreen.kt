package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.phonechange.PhoneChangeContent
import com.team_daytodo.daytodo.feature.mypage.component.profileedit.ProfileSaveButton
import com.team_daytodo.daytodo.feature.mypage.state.PhoneChangeUiState
import com.team_daytodo.daytodo.uikit.dialog.DayTodoMessageDialog
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun PhoneChangeScreen(
    uiState: PhoneChangeUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNewPhoneNumberChange: (String) -> Unit = {},
    onVerificationCodeChange: (String) -> Unit = {},
    onRequestVerificationCodeClick: () -> Unit = {},
    onChangeClick: () -> Unit = {},
    onSuccessDialogDismissed: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = "전화번호 변경", onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = ScreenHorizontalPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                PhoneChangeContent(
                    currentPhoneNumber = uiState.currentPhoneNumber,
                    newPhoneNumber = uiState.newPhoneNumber,
                    verificationCode = uiState.verificationCode,
                    hasRequestedCode = uiState.hasRequestedCode,
                    hasActiveCode = uiState.hasActiveCode,
                    remainingTimeText = uiState.remainingTimeText,
                    isNewPhoneNumberValid = uiState.isNewPhoneNumberValid,
                    onNewPhoneNumberChange = onNewPhoneNumberChange,
                    onVerificationCodeChange = onVerificationCodeChange,
                    onRequestVerificationCodeClick = onRequestVerificationCodeClick,
                )
                Spacer(modifier = Modifier.height(132.dp))
            }

            ProfileSaveButton(
                text = "변경하기",
                onClick = onChangeClick,
                enabled = uiState.canSubmit,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = ScreenHorizontalPadding)
                    .padding(bottom = BottomButtonPadding),
            )
        }
    }

    if (uiState.isChangeSuccess) {
        DayTodoMessageDialog(
            message = "전화번호가 변경되었습니다.",
            onDismiss = onSuccessDialogDismissed,
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun PhoneChangeScreenPreview() {
    DayTodoTheme {
        PhoneChangeScreen(
            uiState = PhoneChangeUiState(currentPhoneNumber = "010-1234-5678"),
        )
    }
}
