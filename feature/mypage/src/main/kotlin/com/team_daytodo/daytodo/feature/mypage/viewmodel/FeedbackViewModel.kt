package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import com.team_daytodo.daytodo.feature.mypage.state.FeedbackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class FeedbackViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    fun onContentChange(content: String) {
        _uiState.update { it.copy(content = content) }
    }

    fun onSubmitClick() {
        if (!_uiState.value.canSubmit) return
        // TODO: BE 연동 담당자가 POST /users/feedback 호출 로직(예: SendFeedbackUseCase)을 채울 것.
        // 이번 작업은 UI 구현 범위라 실제 전송 없이 완료 화면으로만 전환한다.
        _uiState.update { it.copy(isSubmitted = true) }
    }
}
