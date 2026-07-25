package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import com.team_daytodo.daytodo.feature.mypage.state.ProfileEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 프로필 관리 화면 ViewModel.
 * 표시에 필요한 값을 모두 가공해서 UiState 로 내려주고, 화면은 표시만 담당한다.
 */
@HiltViewModel
class ProfileEditViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileEditUiState(isLoading = true))
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    /**
     * TODO: API 연동 시 UseCase 호출로 교체 (viewModelScope.launch + DataResult 처리).
     * 지금은 연동 전 단계라 더미 값으로 State 만 채운다.
     */
    private fun loadProfile() {
        _uiState.value = ProfileEditUiState(
            name = "홍길동",
            nickname = "데이투두",
            email = "daytodo@example.com",
            phoneNumber = "000-0000-0000",
            linkedAccountProvider = "네이버",
            linkedAccountId = "daytodo@naver.com",
            isLoading = false,
        )
    }
}
