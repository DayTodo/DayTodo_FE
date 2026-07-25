package com.team_daytodo.daytodo.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.model.ResetPasswordRequest
import com.team_daytodo.daytodo.domain.auth.usecase.ResetPasswordUseCase
import com.team_daytodo.daytodo.feature.auth.model.ResetPasswordEvent
import com.team_daytodo.daytodo.feature.auth.model.ResetPasswordUiState
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
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ResetPasswordEvent>()
    val event: SharedFlow<ResetPasswordEvent> = _event.asSharedFlow()

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        _uiState.update { it.copy(passwordConfirm = passwordConfirm) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun togglePasswordConfirmVisibility() {
        _uiState.update { it.copy(isPasswordConfirmVisible = !it.isPasswordConfirmVisible) }
    }

    fun resetPassword(verificationToken: String) {
        val currentState = _uiState.value
        if (!currentState.canSubmit) {
            showMessage(currentState.invalidInputMessage())
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            resetPasswordUseCase(
                ResetPasswordRequest(
                    verificationToken = verificationToken,
                    newPassword = currentState.password,
                ),
            ).onSuccess {
                _event.emit(ResetPasswordEvent.PasswordResetCompleted)
            }.onFailure { cause ->
                _event.emit(ResetPasswordEvent.ShowMessage(cause.userFacingMessage(ResetFailureMessage)))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(ResetPasswordEvent.ShowMessage(message))
        }
    }

    private companion object {
        const val ResetFailureMessage = "비밀번호를 변경하지 못했어요."
    }
}
