package com.team_daytodo.daytodo.feature.auth.model

data class ProfileSetupUiState(
    val nickname: String = "",
    val profileImageUri: String? = null,
    val isLoading: Boolean = false,
) {
    val canSubmit: Boolean
        get() = nickname.isNotBlank() && !isLoading
}

sealed interface ProfileSetupEvent {
    data class ShowMessage(val message: String) : ProfileSetupEvent
    data object ProfileSaved : ProfileSetupEvent
}