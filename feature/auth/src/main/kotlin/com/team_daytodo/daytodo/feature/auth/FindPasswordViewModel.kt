package com.team_daytodo.daytodo.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.model.SendPasswordVerificationCodeRequest
import com.team_daytodo.daytodo.domain.auth.model.VerifyPasswordCodeRequest
import com.team_daytodo.daytodo.domain.auth.usecase.SendPasswordVerificationCodeUseCase
import com.team_daytodo.daytodo.domain.auth.usecase.VerifyPasswordCodeUseCase
import com.team_daytodo.daytodo.feature.auth.model.FindPasswordEvent
import com.team_daytodo.daytodo.feature.auth.model.FindPasswordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
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
class FindPasswordViewModel @Inject constructor(
    private val sendPasswordVerificationCodeUseCase: SendPasswordVerificationCodeUseCase,
    private val verifyPasswordCodeUseCase: VerifyPasswordCodeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FindPasswordUiState())
    val uiState: StateFlow<FindPasswordUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FindPasswordEvent>()
    val event: SharedFlow<FindPasswordEvent> = _event.asSharedFlow()

    private var timerJob: Job? = null

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updateVerificationCode(code: String) {
        _uiState.update {
            it.copy(verificationCode = code.filter(Char::isDigit).take(VerificationCodeMaxLength))
        }
    }

    fun sendVerificationCode() {
        val currentState = _uiState.value
        if (!currentState.canSendCode) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSendingCode = true) }
            sendPasswordVerificationCodeUseCase(
                SendPasswordVerificationCodeRequest(email = currentState.email),
            ).onSuccess { result ->
                startTimer(result.expiresInSeconds)
                _event.emit(FindPasswordEvent.ShowMessage("인증코드를 발송했어요."))
            }.onFailure { cause ->
                _event.emit(FindPasswordEvent.ShowMessage(cause.userFacingMessage(SendCodeFailureMessage)))
            }
            _uiState.update { it.copy(isSendingCode = false) }
        }
    }

    fun verifyCode() {
        val currentState = _uiState.value
        if (!currentState.canVerifyCode) {
            showMessage("인증코드 6자리를 입력해 주세요.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isVerifyingCode = true) }
            verifyPasswordCodeUseCase(
                VerifyPasswordCodeRequest(
                    email = currentState.email,
                    code = currentState.verificationCode,
                ),
            ).onSuccess { result ->
                _event.emit(
                    FindPasswordEvent.VerificationCompleted(
                        verificationToken = result.verificationToken,
                    ),
                )
            }.onFailure { cause ->
                _event.emit(FindPasswordEvent.ShowMessage(cause.userFacingMessage(VerificationFailureMessage)))
            }
            _uiState.update { it.copy(isVerifyingCode = false) }
        }
    }

    private fun startTimer(expiresInSeconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (second in expiresInSeconds downTo 0) {
                _uiState.update { it.copy(remainingSeconds = second) }
                if (second > 0) delay(OneSecondMillis)
            }
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(FindPasswordEvent.ShowMessage(message))
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }

    private companion object {
        const val VerificationCodeMaxLength = 6
        const val OneSecondMillis = 1000L
        const val SendCodeFailureMessage = "인증코드를 발송하지 못했어요."
        const val VerificationFailureMessage = "인증코드를 확인하지 못했어요."
    }
}
