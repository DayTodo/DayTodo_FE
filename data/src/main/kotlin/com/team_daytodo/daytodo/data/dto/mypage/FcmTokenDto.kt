package com.team_daytodo.daytodo.data.dto.mypage

import kotlinx.serialization.Serializable

@Serializable
data class RegisterFcmTokenRequest(
    val token: String,
    val platform: String,
)

@Serializable
data class DeleteFcmTokenRequest(
    val token: String,
)
