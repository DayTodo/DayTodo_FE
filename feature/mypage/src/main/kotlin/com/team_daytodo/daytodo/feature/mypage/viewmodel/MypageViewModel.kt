package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.mypage.usecase.GetMypageProfileUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.SetNotificationEnabledUseCase
import com.team_daytodo.daytodo.feature.mypage.state.MypageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val getMypageProfileUseCase: GetMypageProfileUseCase,
    private val setNotificationEnabledUseCase: SetNotificationEnabledUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MypageUiState())
    val uiState: StateFlow<MypageUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun toggleNotification(enabled: Boolean) {
        _uiState.update { it.copy(notificationEnabled = enabled) }
        viewModelScope.launch {
            setNotificationEnabledUseCase(enabled)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getMypageProfileUseCase()
                .onSuccess { profile ->
                    // notificationEnabled는 이번 작업 범위 밖(MypageProfile에서 필드 제거됨) — 기존 UI 상태 값을 그대로 둔다.
                    _uiState.update {
                        it.copy(
                            nickname = profile.nickname,
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}
