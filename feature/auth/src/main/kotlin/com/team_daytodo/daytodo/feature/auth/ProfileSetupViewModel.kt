package com.team_daytodo.daytodo.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.model.ProfileSetupRequest
import com.team_daytodo.daytodo.domain.auth.usecase.SaveProfileUseCase
import com.team_daytodo.daytodo.feature.auth.model.ProfileSetupEvent
import com.team_daytodo.daytodo.feature.auth.model.ProfileSetupUiState
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
class ProfileSetupViewModel @Inject constructor(
    private val saveProfileUseCase: SaveProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileSetupUiState())
    val uiState: StateFlow<ProfileSetupUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileSetupEvent>()
    val event: SharedFlow<ProfileSetupEvent> = _event.asSharedFlow()

    fun updateNickname(nickname: String) {
        _uiState.update { it.copy(nickname = nickname) }
    }

    fun updateProfileImage(uri: String?) {
        _uiState.update { it.copy(profileImageUri = uri) }
    }

    fun saveProfile() {
        val currentState = _uiState.value
        if (!currentState.canSubmit) {
            showMessage("닉네임을 입력해 주세요.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            saveProfileUseCase(
                ProfileSetupRequest(
                    nickname = currentState.nickname,
                    profileImageUri = currentState.profileImageUri,
                ),
            ).onSuccess {
                _event.emit(ProfileSetupEvent.ProfileSaved)
            }.onFailure { cause ->
                _event.emit(ProfileSetupEvent.ShowMessage(cause.userFacingMessage(ProfileFailureMessage)))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(ProfileSetupEvent.ShowMessage(message))
        }
    }

    private companion object {
        const val ProfileFailureMessage = "프로필을 저장하지 못했어요."
    }
}
