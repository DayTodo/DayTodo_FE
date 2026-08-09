package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.mypage.usecase.ChangePhoneNumberUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetMypageProfileUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.RequestPhoneVerificationCodeUseCase
import com.team_daytodo.daytodo.feature.mypage.state.PhoneChangeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PhoneChangeViewModel @Inject constructor(
    private val getMypageProfileUseCase: GetMypageProfileUseCase,
    private val requestPhoneVerificationCodeUseCase: RequestPhoneVerificationCodeUseCase,
    private val changePhoneNumberUseCase: ChangePhoneNumberUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PhoneChangeUiState())
    val uiState: StateFlow<PhoneChangeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadCurrentPhoneNumber()
    }

    fun onNewPhoneNumberChange(value: String) {
        _uiState.update { it.copy(newPhoneNumber = formatPhoneNumber(value)) }
    }

    fun onVerificationCodeChange(value: String) {
        _uiState.update { it.copy(verificationCode = value) }
    }

    fun onRequestVerificationCodeClick() {
        val currentState = _uiState.value
        if (!currentState.isNewPhoneNumberValid) return
        val phoneNumber = currentState.newPhoneNumber

        viewModelScope.launch {
            requestPhoneVerificationCodeUseCase(phoneNumber)
                .onSuccess {
                    _uiState.update { it.copy(hasRequestedCode = true) }
                    startTimer(VerificationCodeExpirySeconds)
                }
                .onFailure { cause -> _uiState.update { it.copy(errorMessage = cause.message) } }
        }
    }

    fun onChangeClick() {
        val currentState = _uiState.value
        if (!currentState.canSubmit) return
        submitPhoneChange(currentState.verificationCode)
    }

    fun onSuccessDialogDismissed() {
        _uiState.update { it.copy(isChangeSuccess = false) }
    }

    private fun loadCurrentPhoneNumber() {
        // MypageProfile에서 phoneNumber 필드가 제거됨(BE 미지원) — 표시할 현재 전화번호 소스가 없어 조회를 생략한다.
    }

    // :feature:auth에 재사용할 만한 전화번호 포맷팅/검증 로직이 없어 새로 작성함.
    // 매 입력마다 숫자만 추출해 처음부터 다시 하이픈을 삽입하므로, 중간 삭제·재입력에도
    // 형식이 깨지지 않는다(기존 하이픈 위치에 의존하지 않음).
    private fun formatPhoneNumber(value: String): String {
        val digits = value.filter(Char::isDigit).take(PhoneNumberDigitLength)
        return when {
            digits.length <= 3 -> digits
            digits.length <= 7 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
            else -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
        }
    }

    private fun submitPhoneChange(verificationCode: String) {
        val phoneNumber = _uiState.value.newPhoneNumber

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            changePhoneNumberUseCase(phoneNumber, verificationCode)
                .onSuccess { updatedPhoneNumber ->
                    timerJob?.cancel()
                    _uiState.update {
                        it.copy(
                            currentPhoneNumber = updatedPhoneNumber,
                            newPhoneNumber = "",
                            verificationCode = "",
                            hasRequestedCode = false,
                            remainingSeconds = 0,
                            isLoading = false,
                            isChangeSuccess = true,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                }
        }
    }

    // :feature:auth FindPasswordViewModel.startTimer와 동일한 카운트다운 패턴.
    // BE의 인증코드 발송 응답에 만료 시간(expiresInSeconds)이 없어(현재 Unit만 반환),
    // 표시 스펙("03:00")에 맞춰 180초를 고정값으로 사용한다.
    private fun startTimer(expiresInSeconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (second in expiresInSeconds downTo 0) {
                _uiState.update { it.copy(remainingSeconds = second) }
                if (second > 0) delay(OneSecondMillis)
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }

    private companion object {
        const val VerificationCodeExpirySeconds = 180
        const val OneSecondMillis = 1000L
        const val PhoneNumberDigitLength = 11
    }
}
