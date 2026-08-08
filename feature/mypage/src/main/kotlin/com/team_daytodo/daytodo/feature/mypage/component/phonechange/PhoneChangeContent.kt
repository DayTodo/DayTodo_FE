package com.team_daytodo.daytodo.feature.mypage.component.phonechange

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhoneChangeContent(
    currentPhoneNumber: String,
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
        CurrentPhoneNumberCard(phoneNumber = currentPhoneNumber)

        Spacer(modifier = Modifier.height(20.dp))
        NewPhoneNumberSection(
            newPhoneNumber = newPhoneNumber,
            verificationCode = verificationCode,
            hasRequestedCode = hasRequestedCode,
            hasActiveCode = hasActiveCode,
            remainingTimeText = remainingTimeText,
            isNewPhoneNumberValid = isNewPhoneNumberValid,
            onNewPhoneNumberChange = onNewPhoneNumberChange,
            onVerificationCodeChange = onVerificationCodeChange,
            onRequestVerificationCodeClick = onRequestVerificationCodeClick,
        )
    }
}
