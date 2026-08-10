package com.team_daytodo.daytodo.feature.mypage.state

data class ProfileEditUiState(
    val name: String = "",
    val nickname: String = "",
    val email: String = "",
    val linkedAccountProvider: String = "",
    val linkedAccountId: String = "",
    val isAccountLinked: Boolean = true,
    val profileImageUri: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
) {
    val canSave: Boolean
        get() = nickname.isNotBlank() && !isSaving
}

sealed interface ProfileEditEvent {
    data class ShowMessage(val message: String) : ProfileEditEvent
    data object ProfileSaved : ProfileEditEvent
}
