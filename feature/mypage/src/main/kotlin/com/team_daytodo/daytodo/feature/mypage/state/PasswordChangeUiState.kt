package com.team_daytodo.daytodo.feature.mypage.state

import com.team_daytodo.daytodo.domain.auth.usecase.isValidPassword

data class PasswordChangeUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val newPasswordConfirm: String = "",
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isNewPasswordConfirmVisible: Boolean = false,
    val isLoading: Boolean = false,
) {
    val isNewPasswordPolicySatisfied: Boolean
        get() = newPassword.isValidPassword()

    val isNewPasswordMatched: Boolean
        get() = newPassword.isNotEmpty() && newPassword == newPasswordConfirm

    val hasPasswordComparison: Boolean
        get() = newPassword.isNotEmpty() && newPasswordConfirm.isNotEmpty()

    val isNewPasswordMismatched: Boolean
        get() = hasPasswordComparison && !isNewPasswordMatched

    val canSubmit: Boolean
        get() = currentPassword.isNotEmpty() &&
            isNewPasswordPolicySatisfied &&
            isNewPasswordMatched &&
            !isLoading
}

fun PasswordChangeUiState.invalidInputMessage(): String =
    when {
        currentPassword.isEmpty() -> "현재 비밀번호를 입력해 주세요."
        !isNewPasswordPolicySatisfied -> "비밀번호는 영문과 숫자를 포함해 8자 이상이어야 해요."
        !isNewPasswordMatched -> "새 비밀번호가 일치하지 않아요."
        else -> "입력값을 다시 확인해 주세요."
    }

sealed interface PasswordChangeEvent {
    data class ShowMessage(val message: String) : PasswordChangeEvent
    data object PasswordChangeCompleted : PasswordChangeEvent
}
