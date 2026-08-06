package com.team_daytodo.daytodo.data.dto.mypage

import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
)

fun ProfileResponse.toDomain(): MypageProfile = MypageProfile(
    userId = userId,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
)
