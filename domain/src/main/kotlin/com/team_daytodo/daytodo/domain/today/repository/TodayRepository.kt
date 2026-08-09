package com.team_daytodo.daytodo.domain.today.repository

import com.team_daytodo.daytodo.domain.today.model.CompletedCourse
import com.team_daytodo.daytodo.domain.today.model.SavedMemoryPhotos
import com.team_daytodo.daytodo.domain.today.model.TodayCourse
import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace

interface TodayRepository {
    suspend fun getTodayCourse(): Result<TodayCourse?>

    suspend fun completeCourse(courseId: Long): Result<CompletedCourse>

    suspend fun saveMemoryPhotos(courseId: Long, imageUrls: List<String>): Result<SavedMemoryPhotos>

    suspend fun reorderCoursePlaces(courseId: Long, orderedCoursePlaceIds: List<Long>): Result<List<TodayCoursePlace>>

    suspend fun deleteCoursePlace(courseId: Long, coursePlaceId: Long): Result<List<TodayCoursePlace>>
}
