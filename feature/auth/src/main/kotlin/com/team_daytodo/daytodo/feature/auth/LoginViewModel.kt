package com.team_daytodo.daytodo.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.core.model.DayTodoException
import com.team_daytodo.daytodo.domain.auth.model.LoginRequest
import com.team_daytodo.daytodo.domain.auth.model.NaverLoginRequest
import com.team_daytodo.daytodo.domain.auth.usecase.LoginUseCase
import com.team_daytodo.daytodo.domain.auth.usecase.LoginWithNaverUseCase
import com.team_daytodo.daytodo.feature.auth.model.LoginEvent
import com.team_daytodo.daytodo.feature.auth.model.LoginUiState
import com.team_daytodo.daytodo.feature.auth.model.invalidInputMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginWithNaverUseCase: LoginWithNaverUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event: SharedFlow<LoginEvent> = _event.asSharedFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateKeepLoggedIn(keepLoggedIn: Boolean) {
        _uiState.update { it.copy(keepLoggedIn = keepLoggedIn) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        val currentState = _uiState.value
        if (currentState.isLoading) return
        if (!currentState.canSubmit) {
            showMessage(currentState.invalidInputMessage())
            return
        }

        loginWith(
            email = currentState.email,
            password = currentState.password,
            keepLoggedIn = currentState.keepLoggedIn,
        )
    }

    fun loginWithNaverToken(naverAccessToken: String?) {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        val token = naverAccessToken?.trim().orEmpty()
        if (token.isBlank()) {
            showNaverLoginFailure(NaverLoginTokenMissingMessage)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loginWithNaverUseCase(
                NaverLoginRequest(
                    naverAccessToken = token,
                    keepLoggedIn = currentState.keepLoggedIn,
                ),
            ).onSuccess { result ->
                _event.emit(LoginEvent.LoginCompleted(result.needsProfileSetup))
            }.onFailure { cause ->
                _event.emit(LoginEvent.ShowMessage(cause.userFacingMessage(NaverLoginFailureMessage)))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun showNaverLoginFailure(message: String) {
        showMessage(message.takeIf(String::isNotBlank) ?: NaverLoginFailureMessage)
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(LoginEvent.ShowMessage(message))
        }
    }

    private fun loginWith(
        email: String,
        password: String,
        keepLoggedIn: Boolean,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loginUseCase(
                LoginRequest(
                    email = email,
                    password = password,
                    keepLoggedIn = keepLoggedIn,
                ),
            ).onSuccess { result ->
                _event.emit(LoginEvent.LoginCompleted(result.needsProfileSetup))
            }.onFailure { cause ->
                _event.emit(LoginEvent.ShowMessage(cause.userFacingMessage(LoginFailureMessage)))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private companion object {
        const val LoginFailureMessage = "\ub85c\uadf8\uc778\uc5d0 \uc2e4\ud328\ud588\uc5b4\uc694. \uc785\ub825\uac12\uc744 \ud655\uc778\ud574 \uc8fc\uc138\uc694."
        const val NaverLoginTokenMissingMessage = "\ub124\uc774\ubc84 \ub85c\uadf8\uc778 \uc815\ubcf4\ub97c \uac00\uc838\uc624\uc9c0 \ubabb\ud588\uc5b4\uc694."
        const val NaverLoginFailureMessage = "\ub124\uc774\ubc84 \ub85c\uadf8\uc778\uc5d0 \uc2e4\ud328\ud588\uc5b4\uc694. \uc7a0\uc2dc \ud6c4 \ub2e4\uc2dc \uc2dc\ub3c4\ud574 \uc8fc\uc138\uc694."
    }
}

internal fun Throwable.userFacingMessage(defaultMessage: String): String =
    when (this) {
        is DayTodoException -> userMessage
        else -> message?.takeIf(String::isNotBlank) ?: defaultMessage
    }
