package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.mypage.usecase.GetMypageProfileUseCase
import com.team_daytodo.daytodo.feature.mypage.state.ProfileEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val getMypageProfileUseCase: GetMypageProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileEditUiState(isLoading = true))
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun onToggleAccountLinkClick() {
        _uiState.update { it.copy(isAccountLinked = !it.isAccountLinked) }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            getMypageProfileUseCase()
                .onSuccess { profile ->
                    _uiState.value = ProfileEditUiState(
                        name = profile.name,
                        nickname = profile.nickname,
                        email = profile.email,
                        phoneNumber = profile.phoneNumber,
                        linkedAccountProvider = profile.linkedAccountProvider,
                        linkedAccountId = profile.linkedAccountId,
                        isLoading = false,
                    )
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                }
        }
    }
}
