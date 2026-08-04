package com.team_daytodo.daytodo.data.dto.today

import com.team_daytodo.daytodo.domain.today.model.CompletedCourse
import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CompleteCourseResponse(
    val courseId: Long,
    val courseStatus: String,
    val completedAt: String,
)

fun CompleteCourseResponse.toDomain(): CompletedCourse = CompletedCourse(
    courseId = courseId,
    courseStatus = courseStatus,
    completedAt = LocalDateTime.parse(completedAt),
)
