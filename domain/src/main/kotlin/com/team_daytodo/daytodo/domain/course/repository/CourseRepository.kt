package com.team_daytodo.daytodo.domain.course.repository

import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.CourseCommentThread
import com.team_daytodo.daytodo.domain.course.model.CourseDetail
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.domain.course.model.CourseUpdateRequest

interface CourseRepository {
    suspend fun getCourseRegions(): Result<List<CourseRegionGroup>>

    suspend fun createCourseRoom(request: CourseCreateRequest): Result<CourseCreateResult>

    suspend fun getUpcomingCourses(): Result<List<CourseSummary>>

    suspend fun getCourseDetail(courseId: String): Result<CourseDetail>

    suspend fun searchPlaces(
        courseId: String,
        query: String,
    ): Result<List<CoursePlaceSearchResult>>

    suspend fun togglePlaceLike(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail>

    suspend fun recommendPlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail>

    suspend fun toggleCoursePlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail>

    suspend fun removeCoursePlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail>

    suspend fun moveCoursePlace(
        courseId: String,
        fromIndex: Int,
        toIndex: Int,
    ): Result<CourseDetail>

    suspend fun updateCourseSettings(request: CourseUpdateRequest): Result<CourseDetail>

    suspend fun getPlaceComments(
        courseId: String,
        placeId: String,
    ): Result<CourseCommentThread>

    suspend fun addPlaceComment(
        courseId: String,
        placeId: String,
        content: String,
    ): Result<CourseCommentThread>
}