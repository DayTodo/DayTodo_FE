package com.team_daytodo.daytodo.feature.auth.model

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val keepLoggedIn: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
) {
    val canSubmit: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}

sealed interface LoginEvent {
    data class ShowMessage(val message: String) : LoginEvent
    data class LoginCompleted(val needsProfileSetup: Boolean) : LoginEvent
}