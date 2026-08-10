package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.mypage.usecase.GetMypageProfileUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.UpdateProfileUseCase
import com.team_daytodo.daytodo.feature.mypage.state.ProfileEditEvent
import com.team_daytodo.daytodo.feature.mypage.state.ProfileEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val getMypageProfileUseCase: GetMypageProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileEditUiState(isLoading = true))
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEditEvent>()
    val event: SharedFlow<ProfileEditEvent> = _event.asSharedFlow()

    init {
        loadProfile()
    }

    fun onToggleAccountLinkClick() {
        _uiState.update { it.copy(isAccountLinked = !it.isAccountLinked) }
    }

    fun onNicknameChanged(nickname: String) {
        _uiState.update { it.copy(nickname = nickname) }
    }

    fun onPhotoSelected(uri: String?) {
        if (uri == null) return
        _uiState.update { it.copy(profileImageUri = uri) }
    }

    fun onSaveClick() {
        val currentState = _uiState.value
        if (!currentState.canSave) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            updateProfileUseCase(currentState.nickname, currentState.profileImageUri)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false) }
                    _event.emit(ProfileEditEvent.ProfileSaved)
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isSaving = false) }
                    _event.emit(ProfileEditEvent.ShowMessage(cause.message ?: "프로필을 저장하지 못했어요."))
                }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            getMypageProfileUseCase()
                .onSuccess { profile ->
                    // name/email/linkedAccountProvider/linkedAccountId는 MypageProfile에서 제거됨(BE 미지원) —
                    // 화면 필드는 그대로 두되 값 소스가 없어 기본값(빈 문자열)으로 남는다.
                    _uiState.value = ProfileEditUiState(
                        nickname = profile.nickname,
                        isLoading = false,
                    )
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                }
        }
    }
}
