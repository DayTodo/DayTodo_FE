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
    // 화면 표시용(서버에 저장된 사진 URL, 혹은 방금 새로 고른 로컬 Uri).
    val profileImageUri: String? = null,
    // 저장 시 실제로 업로드할 값. 사용자가 사진을 새로 고른 경우에만 채워지고,
    // 닉네임만 바꾼 경우엔 null로 남아 기존 서버 사진을 그대로 유지한다.
    val newProfileImageUri: String? = null,
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
