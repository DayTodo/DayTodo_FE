package com.team_daytodo.daytodo.domain.mypage.model

data class MypageProfile(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    // BE 프로필 조회 응답엔 email이 없어 회원가입 시점에 로컬에 저장해둔 값으로 채운다.
    // 재로그인/재설치 등 회원가입을 거치지 않고 세션을 얻은 경우 null일 수 있다.
    val email: String? = null,
)
