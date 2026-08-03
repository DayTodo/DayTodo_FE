package com.team_daytodo.daytodo.feature.mypage.component.phonechange

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun NewPhoneNumberSection(
    newPhoneNumber: String,
    verificationCode: String,
    onNewPhoneNumberChange: (String) -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onRequestVerificationCodeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "새 전화번호",
            style = DayTodoTheme.typography.label2,
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        PhoneChangeTextField(
            value = newPhoneNumber,
            onValueChange = onNewPhoneNumberChange,
            placeholder = "010-1234-5678",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            trailingContent = {
                RequestVerificationCodeButton(onClick = onRequestVerificationCodeClick)
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "인증코드",
            style = DayTodoTheme.typography.label2,
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        PhoneChangeTextField(
            value = verificationCode,
            onValueChange = onVerificationCodeChange,
            placeholder = "4자리",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        )
    }
}
