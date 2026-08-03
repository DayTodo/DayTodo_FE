package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.mypage.usecase.ChangePhoneNumberUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetMypageProfileUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.RequestPhoneVerificationCodeUseCase
import com.team_daytodo.daytodo.feature.mypage.state.PhoneChangeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

    init {
        loadCurrentPhoneNumber()
    }

    fun onNewPhoneNumberChange(value: String) {
        _uiState.update { it.copy(newPhoneNumber = value) }
    }

    // TODO: 화면 스펙에 별도의 "변경하기" 확정 버튼이 없어, 인증코드를 4자리 모두 입력하면 자동으로 변경을 시도하도록 처리함.
    fun onVerificationCodeChange(value: String) {
        _uiState.update { it.copy(verificationCode = value) }
        if (value.length == VerificationCodeLength) {
            submitPhoneChange(value)
        }
    }

    fun onRequestVerificationCodeClick() {
        val phoneNumber = _uiState.value.newPhoneNumber
        if (phoneNumber.isBlank()) return

        viewModelScope.launch {
            requestPhoneVerificationCodeUseCase(phoneNumber)
                .onFailure { cause -> _uiState.update { it.copy(errorMessage = cause.message) } }
        }
    }

    fun onSuccessDialogDismissed() {
        _uiState.update { it.copy(isChangeSuccess = false) }
    }

    private fun loadCurrentPhoneNumber() {
        viewModelScope.launch {
            getMypageProfileUseCase()
                .onSuccess { profile ->
                    _uiState.update { it.copy(currentPhoneNumber = profile.phoneNumber) }
                }
                .onFailure { cause -> _uiState.update { it.copy(errorMessage = cause.message) } }
        }
    }

    private fun submitPhoneChange(verificationCode: String) {
        val phoneNumber = _uiState.value.newPhoneNumber

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            changePhoneNumberUseCase(phoneNumber, verificationCode)
                .onSuccess { updatedPhoneNumber ->
                    _uiState.update {
                        it.copy(
                            currentPhoneNumber = updatedPhoneNumber,
                            newPhoneNumber = "",
                            verificationCode = "",
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

    private companion object {
        const val VerificationCodeLength = 4
    }
}
