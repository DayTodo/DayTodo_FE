package com.team_daytodo.daytodo.data.dto.mypage

import com.team_daytodo.daytodo.domain.mypage.model.MypageProfile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val userId: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String? = null,
)

fun ProfileResponse.toDomain(): MypageProfile = MypageProfile(
    userId = userId,
    email = email,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
)
