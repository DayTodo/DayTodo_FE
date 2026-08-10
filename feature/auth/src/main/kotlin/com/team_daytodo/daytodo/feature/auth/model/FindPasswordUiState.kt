package com.team_daytodo.daytodo.feature.auth.model

import com.team_daytodo.daytodo.domain.auth.usecase.isValidEmail

data class FindPasswordUiState(
    val email: String = "",
    val verificationCode: String = "",
    val remainingSeconds: Int = 0,
    val isSendingCode: Boolean = false,
    val isVerifyingCode: Boolean = false,
) {
    val isEmailValid: Boolean
        get() = email.isValidEmail()

    val canSendCode: Boolean
        get() = !isSendingCode && email.isNotBlank()

    val canVerifyCode: Boolean
        get() = verificationCode.length == VerificationCodeLength && !isVerifyingCode

    val hasActiveCode: Boolean
        get() = remainingSeconds > 0

    val remainingTimeText: String
        get() {
            val minutes = remainingSeconds / SecondsPerMinute
            val seconds = remainingSeconds % SecondsPerMinute
            return "$minutes:${seconds.toString().padStart(2, '0')}"
        }
}

sealed interface FindPasswordEvent {
    data class ShowMessage(val message: String) : FindPasswordEvent
    data class VerificationCompleted(val verificationToken: String) : FindPasswordEvent
}

private const val SecondsPerMinute = 60
private const val VerificationCodeLength = 6