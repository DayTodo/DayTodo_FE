package com.team_daytodo.daytodo.domain.record.repository

import com.team_daytodo.daytodo.domain.record.model.RecordCalendarData
import com.team_daytodo.daytodo.domain.record.model.RecordMemo

interface RecordRepository {
    suspend fun getRecordCalendar(): Result<RecordCalendarData>

    suspend fun addMemo(photoId: String, author: String, content: String): Result<RecordMemo>
}
