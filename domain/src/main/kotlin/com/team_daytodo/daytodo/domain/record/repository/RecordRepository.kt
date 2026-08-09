package com.team_daytodo.daytodo.domain.record.repository

import com.team_daytodo.daytodo.domain.record.model.RecordDiary
import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import com.team_daytodo.daytodo.domain.record.model.RecordPlace
import java.time.LocalDate

interface RecordRepository {
    suspend fun getCoursePlaces(courseId: Long): Result<List<RecordPlace>>

    suspend fun getMemoryPhotos(courseId: Long): Result<List<RecordPhoto>>

    suspend fun getDiaryByDate(date: LocalDate): Result<RecordDiary?>

    suspend fun writeDiary(courseId: Long, content: String): Result<RecordDiary>
}
