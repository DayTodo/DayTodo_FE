package com.team_daytodo.daytodo.feature.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.feature.mypage.state.PasswordChangeEvent
import com.team_daytodo.daytodo.feature.mypage.state.PasswordChangeUiState
import com.team_daytodo.daytodo.feature.mypage.state.invalidInputMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PasswordChangeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(PasswordChangeUiState())
    val uiState: StateFlow<PasswordChangeUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<PasswordChangeEvent>()
    val event: SharedFlow<PasswordChangeEvent> = _event.asSharedFlow()

    fun updateCurrentPassword(currentPassword: String) {
        _uiState.update { it.copy(currentPassword = currentPassword) }
    }

    fun updateNewPassword(newPassword: String) {
        _uiState.update { it.copy(newPassword = newPassword) }
    }

    fun updateNewPasswordConfirm(newPasswordConfirm: String) {
        _uiState.update { it.copy(newPasswordConfirm = newPasswordConfirm) }
    }

    fun toggleCurrentPasswordVisibility() {
        _uiState.update { it.copy(isCurrentPasswordVisible = !it.isCurrentPasswordVisible) }
    }

    fun toggleNewPasswordVisibility() {
        _uiState.update { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }
    }

    fun toggleNewPasswordConfirmVisibility() {
        _uiState.update { it.copy(isNewPasswordConfirmVisible = !it.isNewPasswordConfirmVisible) }
    }

    fun changePassword() {
        val currentState = _uiState.value
        if (!currentState.canSubmit) {
            showMessage(currentState.invalidInputMessage())
            return
        }


        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(SimulatedRequestDelayMillis)
            Log.w(LogTag, "BE 미지원: 인증된 비밀번호 변경 API가 없어 요청을 보내지 않고 실패 처리합니다.")
            _event.emit(PasswordChangeEvent.ShowMessage(ChangePasswordFailureMessage))
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(PasswordChangeEvent.ShowMessage(message))
        }
    }

    private companion object {
        const val LogTag = "PasswordChangeViewModel"
        const val SimulatedRequestDelayMillis = 600L
        const val ChangePasswordFailureMessage = "비밀번호를 변경하지 못했어요. 잠시 후 다시 시도해 주세요."
    }
}
