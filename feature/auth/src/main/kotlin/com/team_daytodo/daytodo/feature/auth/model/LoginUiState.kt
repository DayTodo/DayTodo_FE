package com.team_daytodo.daytodo.feature.auth.model

import com.team_daytodo.daytodo.domain.auth.usecase.isValidEmail
import com.team_daytodo.daytodo.domain.auth.usecase.isValidPassword

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val keepLoggedIn: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
) {
    val isEmailValid: Boolean
        get() = email.isValidEmail()

    val isPasswordPolicySatisfied: Boolean
        get() = password.isValidPassword()

    val canSubmit: Boolean
        get() = isEmailValid && isPasswordPolicySatisfied && !isLoading
}

fun LoginUiState.invalidInputMessage(): String =
    when {
        email.isBlank() -> "\uc774\uba54\uc77c\uc744 \uc785\ub825\ud574 \uc8fc\uc138\uc694."
        !isEmailValid -> "\uc62c\ubc14\ub978 \uc774\uba54\uc77c\uc744 \uc785\ub825\ud574 \uc8fc\uc138\uc694."
        password.isBlank() -> "\ube44\ubc00\ubc88\ud638\ub97c \uc785\ub825\ud574 \uc8fc\uc138\uc694."
        !isPasswordPolicySatisfied -> "\ube44\ubc00\ubc88\ud638\ub294 \uc601\ubb38\uacfc \uc22b\uc790\ub97c \ud3ec\ud568\ud574 8\uc790 \uc774\uc0c1\uc774\uc5b4\uc57c \ud574\uc694."
        else -> "\uc785\ub825\uac12\uc744 \ub2e4\uc2dc \ud655\uc778\ud574 \uc8fc\uc138\uc694."
    }

sealed interface LoginEvent {
    data class ShowMessage(val message: String) : LoginEvent
    data class LoginCompleted(val needsProfileSetup: Boolean) : LoginEvent
}
