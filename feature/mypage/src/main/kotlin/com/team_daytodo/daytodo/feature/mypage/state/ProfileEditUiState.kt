package com.team_daytodo.daytodo.feature.mypage.state

data class ProfileEditUiState(
    val name: String = "",
    val nickname: String = "",
    val email: String = "",
    val linkedAccountProvider: String = "",
    val linkedAccountId: String = "",
    // BE에 연동 상태 조회 API가 없어(전수 확인) 실제 연동 여부를 알 방법이 없다. true로 기본값을
    // 두면 항상 "연동됨"으로 보여 진짜 "연동하기" 버튼이 가려지므로, 미연동으로 시작한다.
    val isAccountLinked: Boolean = false,
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
