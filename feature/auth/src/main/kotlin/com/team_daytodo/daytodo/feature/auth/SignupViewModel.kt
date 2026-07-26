package com.team_daytodo.daytodo.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.model.SignupRequest
import com.team_daytodo.daytodo.domain.auth.usecase.SignupUseCase
import com.team_daytodo.daytodo.feature.auth.model.SignupEvent
import com.team_daytodo.daytodo.feature.auth.model.SignupUiState
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
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignupEvent>()
    val event: SharedFlow<SignupEvent> = _event.asSharedFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        _uiState.update { it.copy(passwordConfirm = passwordConfirm) }
    }

    fun updateTermsAgreement(agreed: Boolean) {
        _uiState.update { it.copy(agreedToTerms = agreed) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun togglePasswordConfirmVisibility() {
        _uiState.update { it.copy(isPasswordConfirmVisible = !it.isPasswordConfirmVisible) }
    }

    fun signup() {
        val currentState = _uiState.value
        if (!currentState.canSubmit) {
            showMessage(currentState.invalidInputMessage())
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            signupUseCase(
                SignupRequest(
                    email = currentState.email,
                    password = currentState.password,
                    agreedToTerms = currentState.agreedToTerms,
                ),
            ).onSuccess { result ->
                _event.emit(SignupEvent.SignupCompleted(result.needsProfileSetup))
            }.onFailure { cause ->
                _event.emit(SignupEvent.ShowMessage(cause.userFacingMessage(SignupFailureMessage)))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(SignupEvent.ShowMessage(message))
        }
    }

    private companion object {
        const val SignupFailureMessage = "회원가입에 실패했어요. 입력값을 확인해 주세요."
    }
}
