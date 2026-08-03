package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.phonechange.PhoneChangeContent
import com.team_daytodo.daytodo.feature.mypage.state.PhoneChangeUiState
import com.team_daytodo.daytodo.uikit.dialog.DayTodoMessageDialog
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val HorizontalPadding = 20.dp

@Composable
fun PhoneChangeScreen(
    uiState: PhoneChangeUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNewPhoneNumberChange: (String) -> Unit = {},
    onVerificationCodeChange: (String) -> Unit = {},
    onRequestVerificationCodeClick: () -> Unit = {},
    onSuccessDialogDismissed: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = "전화번호 변경", onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = HorizontalPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            PhoneChangeContent(
                currentPhoneNumber = uiState.currentPhoneNumber,
                newPhoneNumber = uiState.newPhoneNumber,
                verificationCode = uiState.verificationCode,
                onNewPhoneNumberChange = onNewPhoneNumberChange,
                onVerificationCodeChange = onVerificationCodeChange,
                onRequestVerificationCodeClick = onRequestVerificationCodeClick,
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
