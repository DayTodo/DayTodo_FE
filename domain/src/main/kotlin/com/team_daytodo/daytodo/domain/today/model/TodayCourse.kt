package com.team_daytodo.daytodo.domain.today.model

data class TodayCourse(
    val hasCourse: Boolean,
    val members: List<TodayCourseMember>,
    val places: List<TodayCoursePlace>,
)

data class TodayCourseMember(
    val id: String,
    val name: String,
)

data class TodayCoursePlace(
    val id: String,
    val name: String,
    val category: String,
)
