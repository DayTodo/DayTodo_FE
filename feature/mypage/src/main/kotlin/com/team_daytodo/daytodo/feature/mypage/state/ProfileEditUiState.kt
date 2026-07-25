package com.team_daytodo.daytodo.feature.mypage.state

/**
 * 프로필 관리 화면 상태.
 *
 * 이름 / 닉네임 / 이메일 / 전화번호 / 연동 계정(제공자, 아이디)은 모두 서버에서 받아오는
 * 사용자 데이터라 화면 파라미터가 아니라 ViewModel 이 소유한다.
 * 화면은 여기 담긴 값을 그대로 표시만 하며, 가공은 ViewModel 에서 끝낸 상태로 내려온다.
 */
data class ProfileEditUiState(
    val name: String = "",
    val nickname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val linkedAccountProvider: String = "",
    val linkedAccountId: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
