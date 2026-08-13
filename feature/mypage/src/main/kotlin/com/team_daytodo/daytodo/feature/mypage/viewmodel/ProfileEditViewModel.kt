package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.model.LinkNaverRequest
import com.team_daytodo.daytodo.domain.auth.usecase.LinkNaverUseCase
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
    private val linkNaverUseCase: LinkNaverUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileEditUiState(isLoading = true))
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEditEvent>()
    val event: SharedFlow<ProfileEditEvent> = _event.asSharedFlow()

    init {
        loadProfile()
    }

    // 연동 해제 API가 BE에 없어(전수 확인) 로컬 상태만 되돌린다.
    fun onUnlinkAccountClick() {
        _uiState.update { it.copy(isAccountLinked = false) }
    }

    fun linkNaverAccount(naverAccessToken: String?) {
        val token = naverAccessToken?.trim().orEmpty()
        if (token.isBlank()) {
            showNaverLinkFailure("네이버 인증 정보를 가져오지 못했어요.")
            return
        }

        viewModelScope.launch {
            linkNaverUseCase(LinkNaverRequest(providerToken = token))
                .onSuccess {
                    _uiState.update { it.copy(isAccountLinked = true, linkedAccountProvider = "네이버") }
                    _event.emit(ProfileEditEvent.ShowMessage("네이버 계정을 연동했어요."))
                }
                .onFailure { cause ->
                    _event.emit(ProfileEditEvent.ShowMessage(cause.message ?: "네이버 계정 연동에 실패했어요."))
                }
        }
    }

    fun showNaverLinkFailure(message: String) {
        viewModelScope.launch {
            _event.emit(ProfileEditEvent.ShowMessage(message.takeIf(String::isNotBlank) ?: "네이버 계정 연동에 실패했어요."))
        }
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
                    // name/linkedAccountProvider/linkedAccountId는 BE가 지원하지 않아 값 소스가 없다.
                    _uiState.value = ProfileEditUiState(
                        nickname = profile.nickname,
                        email = profile.email,
                        isLoading = false,
                    )
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                }
        }
    }
}
