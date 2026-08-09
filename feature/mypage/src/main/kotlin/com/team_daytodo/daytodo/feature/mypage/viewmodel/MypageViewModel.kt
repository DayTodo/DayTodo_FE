package com.team_daytodo.daytodo.feature.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.usecase.ClearAuthTokensUseCase
import com.team_daytodo.daytodo.domain.auth.usecase.GetRefreshTokenUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetMypageProfileUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetNotificationSettingsUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.LogoutUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.SetNotificationEnabledUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.WithdrawUseCase
import com.team_daytodo.daytodo.feature.mypage.state.MypageDialogState
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
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val setNotificationEnabledUseCase: SetNotificationEnabledUseCase,
    private val withdrawUseCase: WithdrawUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val clearAuthTokensUseCase: ClearAuthTokensUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MypageUiState())
    val uiState: StateFlow<MypageUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
        loadNotificationSettings()
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

    private fun loadNotificationSettings() {
        viewModelScope.launch {
            getNotificationSettingsUseCase()
                .onSuccess { pushEnabled ->
                    _uiState.update { it.copy(notificationEnabled = pushEnabled) }
                }
        }
    }

    fun onDialogStateChange(state: MypageDialogState) {
        _uiState.update { it.copy(dialogState = state) }
    }

    fun confirmWithdraw() {
        viewModelScope.launch {
            withdrawUseCase()
                .onSuccess {
                    _uiState.update { it.copy(dialogState = MypageDialogState.WithdrawDone) }
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(errorMessage = cause.message) }
                }
        }
    }

    fun confirmLogout() {
        viewModelScope.launch {
            val refreshToken = getRefreshTokenUseCase()
            if (refreshToken.isNullOrEmpty()) {
                Log.w(LogTag, "로그아웃: 저장된 refreshToken이 없어 API 호출 없이 로컬 로그아웃만 진행합니다.")
            } else {
                logoutUseCase(refreshToken)
                    .onFailure { cause ->
                        Log.w(LogTag, "로그아웃 API 호출 실패, 로컬 로그아웃은 계속 진행합니다.", cause)
                    }
            }
            clearAuthTokensUseCase()
            _uiState.update { it.copy(dialogState = MypageDialogState.LogoutDone) }
        }
    }

    private companion object {
        const val LogTag = "MypageViewModel"
    }
}
