package com.team_daytodo.daytodo.feature.today.state

import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace

data class TodayUiState(
    val hasCourse: Boolean = false,
    val courseId: Long? = null,
    val members: List<CourseMember> = emptyList(),
    val places: List<CoursePlace> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedMemoryPhotoUris: List<String> = emptyList(),
    val isSavingMemoryPhotos: Boolean = false,
)
