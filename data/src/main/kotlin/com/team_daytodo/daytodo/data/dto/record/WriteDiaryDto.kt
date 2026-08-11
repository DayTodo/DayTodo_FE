package com.team_daytodo.daytodo.data.dto.record

import com.team_daytodo.daytodo.domain.record.model.RecordDiary
import kotlinx.serialization.Serializable

@Serializable
data class WriteDiaryRequest(
    val courseId: Long,
    val content: String,
)

@Serializable
data class WriteDiaryResponse(
    val diaryId: Long,
    val courseId: Long,
    val diaryDate: String,
    val content: String?,
)

fun WriteDiaryResponse.toDomain(): RecordDiary = RecordDiary(
    diaryId = diaryId,
    courseId = courseId,
    content = content.orEmpty(),
)
