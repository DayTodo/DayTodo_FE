package com.team_daytodo.daytodo.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.model.EmailCheckRequest
import com.team_daytodo.daytodo.domain.auth.model.SignupRequest
import com.team_daytodo.daytodo.domain.auth.usecase.CheckEmailUseCase
import com.team_daytodo.daytodo.domain.auth.usecase.SignupUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetPoliciesUseCase
import com.team_daytodo.daytodo.feature.auth.model.SignupPolicyDialogUiState
import com.team_daytodo.daytodo.feature.auth.model.SignupEvent
import com.team_daytodo.daytodo.feature.auth.model.SignupUiState
import com.team_daytodo.daytodo.feature.auth.model.invalidInputMessage
import com.team_daytodo.daytodo.feature.mypage.model.PolicyContent
import com.team_daytodo.daytodo.feature.mypage.model.PolicyDocuments
import com.team_daytodo.daytodo.feature.mypage.model.RemotePolicyField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
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
    private val checkEmailUseCase: CheckEmailUseCase,
    private val signupUseCase: SignupUseCase,
    private val getPoliciesUseCase: GetPoliciesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignupEvent>()
    val event: SharedFlow<SignupEvent> = _event.asSharedFlow()

    private var policyDocumentRequestId = 0
    private var policyDocumentJob: Job? = null

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

    fun showPolicyDocument(index: Int) {
        val document = PolicyDocuments.getOrNull(index) ?: return
        val requestId = nextPolicyDocumentRequestId()
        when (val content = document.content) {
            is PolicyContent.Static -> {
                _uiState.update {
                    it.copy(
                        policyDialog = SignupPolicyDialogUiState(
                            title = document.title,
                            body = content.body,
                        ),
                    )
                }
            }
            is PolicyContent.Remote -> {
                _uiState.update {
                    it.copy(
                        policyDialog = SignupPolicyDialogUiState(
                            title = document.title,
                            isLoading = true,
                        ),
                    )
                }
                policyDocumentJob = viewModelScope.launch {
                    getPoliciesUseCase()
                        .onSuccess { policies ->
                            if (!isActivePolicyDocumentRequest(requestId)) return@onSuccess
                            val body = when (content.field) {
                                RemotePolicyField.TermsOfService -> policies.termsOfService
                                RemotePolicyField.PrivacyPolicy -> policies.privacyPolicy
                            }
                            _uiState.update {
                                it.copy(
                                    policyDialog = SignupPolicyDialogUiState(
                                        title = document.title,
                                        body = body,
                                    ),
                                )
                            }
                        }
                        .onFailure {
                            if (!isActivePolicyDocumentRequest(requestId)) return@onFailure
                            _uiState.update {
                                it.copy(
                                    policyDialog = SignupPolicyDialogUiState(
                                        title = document.title,
                                        isError = true,
                                    ),
                                )
                            }
                        }
                }
            }
        }
    }

    fun dismissPolicyDocument() {
        invalidatePolicyDocumentRequest()
        _uiState.update { it.copy(policyDialog = null) }
    }

    fun signup() {
        val currentState = _uiState.value
        if (!currentState.canSubmit) {
            showMessage(currentState.invalidInputMessage())
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val emailCheckResult = checkEmailUseCase(
                EmailCheckRequest(email = currentState.email),
            ).getOrElse { cause ->
                _event.emit(SignupEvent.ShowMessage(cause.userFacingMessage(EmailCheckFailureMessage)))
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            if (!emailCheckResult.available) {
                _event.emit(SignupEvent.ShowMessage(EmailDuplicatedMessage))
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            signupUseCase(
                SignupRequest(
                    email = currentState.email,
                    password = currentState.password,
                    agreedToTerms = currentState.agreedToTerms,
                ),
            ).onSuccess { result ->
                _event.emit(SignupEvent.ShowMessage(SignupSuccessMessage))
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

    private fun nextPolicyDocumentRequestId(): Int {
        policyDocumentJob?.cancel()
        policyDocumentRequestId += 1
        return policyDocumentRequestId
    }

    private fun invalidatePolicyDocumentRequest() {
        policyDocumentJob?.cancel()
        policyDocumentJob = null
        policyDocumentRequestId += 1
    }

    private fun isActivePolicyDocumentRequest(requestId: Int): Boolean =
        policyDocumentRequestId == requestId && _uiState.value.policyDialog != null

    private companion object {
        const val EmailCheckFailureMessage = "이메일 중복 확인에 실패했어요."
        const val EmailDuplicatedMessage = "이미 가입된 이메일입니다."
        const val SignupSuccessMessage = "가입이 완료됐어요. 이메일 인증 후 로그인해 주세요."
        const val SignupFailureMessage = "회원가입에 실패했어요. 입력값을 확인해 주세요."
    }
}
