package com.team_daytodo.daytodo.feature.auth.model

import com.team_daytodo.daytodo.domain.auth.usecase.isValidEmail

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val keepLoggedIn: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
) {
    val canSubmit: Boolean
        get() = email.isValidEmail() && password.isNotBlank() && !isLoading
}

fun LoginUiState.invalidInputMessage(): String =
    when {
        email.isBlank() -> "이메일을 입력해주세요"
        !email.isValidEmail() -> "올바른 이메일을 입력해 주세요."
        password.isBlank() -> "비밀번호를 입력해주세요"
        else -> "입력값을 다시 확인해 주세요."
    }

sealed interface LoginEvent {
    data class ShowMessage(val message: String) : LoginEvent
    data class LoginCompleted(val needsProfileSetup: Boolean) : LoginEvent
}
