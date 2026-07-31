package com.team_daytodo.daytodo.domain.record.model

import java.time.LocalDate

data class RecordCalendarData(
    val courseDates: Set<LocalDate>,
    val photoCountsByDate: Map<LocalDate, Int>,
    val coursesByDate: Map<LocalDate, List<RecordVisitedCourse>>,
    val memosByPhotoId: Map<String, List<RecordMemo>>,
)

data class RecordVisitedCourse(
    val id: String,
    val title: String,
)

data class RecordMemo(
    val id: String,
    val author: String,
    val content: String,
)
