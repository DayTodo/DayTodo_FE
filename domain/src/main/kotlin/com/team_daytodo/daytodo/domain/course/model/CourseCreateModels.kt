package com.team_daytodo.daytodo.domain.course.model

import com.team_daytodo.daytodo.core.model.Relationship

data class CourseCreateRequest(
    val roomName: String,
    val region: String,
    val date: CourseDate,
    val minBudget: Int,
    val maxBudget: Int,
    val relationship: Relationship,
)

data class CourseCreateResult(
    val inviteLink: String,
)

data class CourseDate(
    val year: Int,
    val month: Int,
    val day: Int,
) {
    fun displayText(): String = "$year.$month.$day"
}

data class CourseRegionGroup(
    val name: String,
    val children: List<String>,
)
