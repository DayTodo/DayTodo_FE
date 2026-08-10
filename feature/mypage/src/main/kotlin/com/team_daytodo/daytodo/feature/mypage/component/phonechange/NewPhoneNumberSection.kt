package com.team_daytodo.daytodo.feature.mypage.component.phonechange

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun NewPhoneNumberSection(
    newPhoneNumber: String,
    verificationCode: String,
    hasRequestedCode: Boolean,
    hasActiveCode: Boolean,
    remainingTimeText: String,
    isNewPhoneNumberValid: Boolean,
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
            placeholder = "새 전화번호를 입력해주세요",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            trailingContent = {
                RequestVerificationCodeButton(
                    hasRequestedCode = hasRequestedCode,
                    enabled = isNewPhoneNumberValid,
                    onClick = onRequestVerificationCodeClick,
                )
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
            trailingContent = if (hasActiveCode) {
                {
                    Text(
                        text = remainingTimeText,
                        style = DayTodoTheme.typography.caption1.copy(
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.sp,
                        ),
                        color = TimerTextColor,
                        modifier = Modifier.padding(end = 12.dp),
                    )
                }
            } else {
                null
            },
        )
    }
}

private val TimerTextColor = Color(0xFFFF5555)
