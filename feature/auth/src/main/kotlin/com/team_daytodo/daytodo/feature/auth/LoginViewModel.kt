package com.team_daytodo.daytodo.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.model.LoginRequest
import com.team_daytodo.daytodo.domain.auth.usecase.LoginUseCase
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

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(LoginEvent.ShowMessage(message))
        }
    }

    fun loginWithNaver() {
        if (_uiState.value.isLoading) return

        loginWith(
            email = NaverDummyEmail,
            password = SocialLoginDummyPassword,
            keepLoggedIn = true,
        )
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
        const val LoginFailureMessage = "로그인에 실패했어요. 입력값을 확인해 주세요."
        const val NaverDummyEmail = "naver@daytodo.com"
        const val SocialLoginDummyPassword = "social-login"
    }
}

internal fun Throwable.userFacingMessage(defaultMessage: String): String =
    message?.takeIf(String::isNotBlank) ?: defaultMessage
