package com.team_daytodo.daytodo.data.dto.today

import kotlinx.serialization.Serializable

@Serializable
data class CompleteCourseResponse(
    val courseId: Long,
    val courseStatus: String,
    val completedAt: String,
)
