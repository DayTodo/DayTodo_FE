package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import com.team_daytodo.daytodo.feature.mypage.state.ProfileEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ProfileEditViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileEditUiState(isLoading = true))
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

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
