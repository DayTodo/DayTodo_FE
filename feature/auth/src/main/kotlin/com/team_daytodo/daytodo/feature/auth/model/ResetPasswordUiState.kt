package com.team_daytodo.daytodo.feature.auth.model

import com.team_daytodo.daytodo.domain.auth.usecase.isValidPassword

data class ResetPasswordUiState(
    val password: String = "",
    val passwordConfirm: String = "",
    val isPasswordVisible: Boolean = false,
    val isPasswordConfirmVisible: Boolean = false,
    val isLoading: Boolean = false,
) {
    val isPasswordPolicySatisfied: Boolean
        get() = password.isValidPassword()

    val isPasswordMatched: Boolean
        get() = password.isNotEmpty() && password == passwordConfirm

    val hasPasswordComparison: Boolean
        get() = password.isNotEmpty() && passwordConfirm.isNotEmpty()

    val isPasswordMismatched: Boolean
        get() = hasPasswordComparison && !isPasswordMatched

    val canSubmit: Boolean
        get() = isPasswordPolicySatisfied && isPasswordMatched && !isLoading
}

fun ResetPasswordUiState.invalidInputMessage(): String =
    when {
        !isPasswordPolicySatisfied -> "비밀번호는 영문과 숫자를 포함해 8자 이상이어야 해요."
        !isPasswordMatched -> "비밀번호 확인이 일치하지 않아요."
        else -> "입력값을 다시 확인해 주세요."
    }

sealed interface ResetPasswordEvent {
    data class ShowMessage(val message: String) : ResetPasswordEvent
    data object PasswordResetCompleted : ResetPasswordEvent
}