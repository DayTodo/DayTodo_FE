package com.team_daytodo.daytodo.feature.mypage.state

data class PhoneChangeUiState(
    val currentPhoneNumber: String = "",
    val newPhoneNumber: String = "",
    val verificationCode: String = "",
    val hasRequestedCode: Boolean = false,
    val remainingSeconds: Int = 0,
    val isLoading: Boolean = false,
    val isChangeSuccess: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasActiveCode: Boolean
        get() = remainingSeconds > 0

    val remainingTimeText: String
        get() {
            val minutes = remainingSeconds / SecondsPerMinute
            val seconds = remainingSeconds % SecondsPerMinute
            return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        }

    val isVerificationCodeComplete: Boolean
        get() = verificationCode.length == VerificationCodeLength

    val isNewPhoneNumberValid: Boolean
        get() = newPhoneNumber.count(Char::isDigit) == PhoneNumberDigitLength

    val canSubmit: Boolean
        get() = isVerificationCodeComplete && !isLoading
}

private const val SecondsPerMinute = 60
private const val VerificationCodeLength = 4
private const val PhoneNumberDigitLength = 11
