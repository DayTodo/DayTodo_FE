package com.team_daytodo.daytodo.data.dto.mypage

import kotlinx.serialization.Serializable

@Serializable
data class SendFeedbackRequest(
    val content: String,
)
