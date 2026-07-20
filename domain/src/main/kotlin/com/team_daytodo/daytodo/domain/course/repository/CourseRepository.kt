package com.team_daytodo.daytodo.domain.course.repository

import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup

interface CourseRepository {
    suspend fun getCourseRegions(): Result<List<CourseRegionGroup>>

    suspend fun createCourseRoom(request: CourseCreateRequest): Result<CourseCreateResult>
}
