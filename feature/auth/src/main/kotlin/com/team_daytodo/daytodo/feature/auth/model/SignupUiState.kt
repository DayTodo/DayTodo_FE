package com.team_daytodo.daytodo.feature.auth.model

import com.team_daytodo.daytodo.domain.auth.usecase.isValidEmail
import com.team_daytodo.daytodo.domain.auth.usecase.isValidPassword

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val agreedToTerms: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isPasswordConfirmVisible: Boolean = false,
    val isLoading: Boolean = false,
) {
    val isEmailValid: Boolean
        get() = email.isValidEmail()

    val isPasswordPolicySatisfied: Boolean
        get() = password.isValidPassword()

    val isPasswordMatched: Boolean
        get() = password.isNotEmpty() && password == passwordConfirm

    val canSubmit: Boolean
        get() = isEmailValid &&
                isPasswordPolicySatisfied &&
                isPasswordMatched &&
                agreedToTerms &&
                !isLoading
}

fun SignupUiState.invalidInputMessage(): String =
    when {
        email.isBlank() -> "이메일을 입력해주세요"
        !isEmailValid -> "올바른 이메일을 입력해 주세요."
        password.isBlank() -> "비밀번호를 입력해주세요"
        !isPasswordPolicySatisfied -> "비밀번호는 영문과 숫자를 포함해 8자 이상이어야 해요."
        passwordConfirm.isBlank() -> "비밀번호 확인을 입력해주세요"
        !isPasswordMatched -> "비밀번호 확인이 일치하지 않아요."
        !agreedToTerms -> "이용약관 및 개인정보처리방침에 동의해 주세요."
        else -> "입력값을 다시 확인해 주세요."
    }

sealed interface SignupEvent {
    data class ShowMessage(val message: String) : SignupEvent
    data class SignupCompleted(val needsProfileSetup: Boolean) : SignupEvent
}
