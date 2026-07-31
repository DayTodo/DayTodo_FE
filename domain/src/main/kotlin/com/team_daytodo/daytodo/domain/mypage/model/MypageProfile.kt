package com.team_daytodo.daytodo.domain.mypage.model

data class MypageProfile(
    val name: String,
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    val linkedAccountProvider: String,
    val linkedAccountId: String,
    val notificationEnabled: Boolean,
)
