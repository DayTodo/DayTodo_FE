package com.team_daytodo.daytodo.data.dto.record

import com.team_daytodo.daytodo.domain.record.model.RecordDiary
import kotlinx.serialization.Serializable

@Serializable
data class MemoryByDateDto(
    val diaryId: Long,
    val courseId: Long,
    val courseName: String,
    val diaryDate: String,
    val content: String?,
    val photos: List<PhotoDto>,
)

fun MemoryByDateDto.toDomain(): RecordDiary = RecordDiary(
    diaryId = diaryId,
    courseId = courseId,
    content = content.orEmpty(),
)
