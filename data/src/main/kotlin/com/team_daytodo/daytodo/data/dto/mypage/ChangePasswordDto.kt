package com.team_daytodo.daytodo.data.dto.mypage

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
)
