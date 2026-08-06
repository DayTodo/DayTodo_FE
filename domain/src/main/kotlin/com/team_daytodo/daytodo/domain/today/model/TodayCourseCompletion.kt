package com.team_daytodo.daytodo.domain.today.model

import java.time.LocalDateTime

data class CompletedCourse(
    val courseId: Long,
    val courseStatus: String,
    val completedAt: LocalDateTime,
)

data class SavedMemoryPhotos(
    val savedCount: Int,
    val photos: List<MemoryPhoto>,
)

data class MemoryPhoto(
    val memoryPhotoId: Long,
    val imageUrl: String,
    val photoOrder: Int,
)
