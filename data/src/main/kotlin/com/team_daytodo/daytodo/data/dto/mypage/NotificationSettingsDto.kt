package com.team_daytodo.daytodo.data.dto.mypage

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingsResponse(
    val pushEnabled: Boolean,
)

@Serializable
data class UpdateNotificationSettingsRequest(
    val pushEnabled: Boolean,
)
