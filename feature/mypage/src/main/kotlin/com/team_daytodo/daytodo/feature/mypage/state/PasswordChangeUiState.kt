package com.team_daytodo.daytodo.feature.mypage.state

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
        get() = newPassword.matches(NewPasswordPolicyRegex)

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
        !isNewPasswordPolicySatisfied -> NewPasswordPolicyMessage
        !isNewPasswordMatched -> "새 비밀번호가 일치하지 않아요."
        else -> "입력값을 다시 확인해 주세요."
    }

sealed interface PasswordChangeEvent {
    data class ShowMessage(val message: String) : PasswordChangeEvent
    data object PasswordChangeCompleted : PasswordChangeEvent
}

// PATCH /users/password(BE)의 새 비밀번호 정규식과 동일하게 맞춘 전용 검증.
// signup/resetPassword가 공유하는 isValidPassword()는 아직 실제 API에 연결되지 않은 더미
// 플로우라 BE 규칙과 일치한다고 보장할 수 없어 건드리지 않고, 이 화면 전용으로 분리했다.
private val NewPasswordPolicyRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#\$%^&*()_+]{8,}$")

private const val NewPasswordPolicyMessage =
    "영문, 숫자를 포함해 8자 이상, 특수문자는 !@#\$%^&*()_+ 만 사용할 수 있어요."
