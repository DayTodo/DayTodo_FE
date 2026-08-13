package com.team_daytodo.daytodo.domain.mypage.model

data class MypageProfile(
    val userId: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
)
