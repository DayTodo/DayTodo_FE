package com.team_daytodo.daytodo.domain.today.repository

import com.team_daytodo.daytodo.domain.today.model.CompletedCourse
import com.team_daytodo.daytodo.domain.today.model.SavedMemoryPhotos
import com.team_daytodo.daytodo.domain.today.model.TodayCourse

interface TodayRepository {
    suspend fun getTodayCourse(): Result<TodayCourse?>

    suspend fun completeCourse(courseId: Long): Result<CompletedCourse>

    suspend fun saveMemoryPhotos(courseId: Long, imageUrls: List<String>): Result<SavedMemoryPhotos>
}
