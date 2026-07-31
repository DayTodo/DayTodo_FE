package com.team_daytodo.daytodo.feature.today.state

import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace

data class TodayUiState(
    val hasCourse: Boolean = false,
    val members: List<CourseMember> = emptyList(),
    val places: List<CoursePlace> = emptyList(),
    val isLoading: Boolean = false,
)
