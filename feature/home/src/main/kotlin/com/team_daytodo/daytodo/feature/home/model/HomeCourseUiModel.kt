package com.team_daytodo.daytodo.feature.home.model

import com.team_daytodo.daytodo.core.model.Relationship

data class TodayCourse(
    val date: String,
    val title: String,
    val relationship: Relationship,
    val members: List<CourseMember>,
)

data class UpcomingCourse(
    val relationship: Relationship,
    val date: String,
)

data class CreatedCourse(
    val title: String,
    val date: String,
    val memberCount: Int,
    val dDay: String,
    val relationship: Relationship,
)

data class CourseMember(
    val name: String,
    val profileImage: Int? = null,
)
