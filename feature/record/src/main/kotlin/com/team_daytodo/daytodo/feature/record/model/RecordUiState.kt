package com.team_daytodo.daytodo.feature.record.model

import com.team_daytodo.daytodo.domain.calendar.model.CalendarCourse
import com.team_daytodo.daytodo.domain.calendar.model.ParticipantType
import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import com.team_daytodo.daytodo.domain.record.model.RecordPlace
import java.time.LocalDate

data class RecordUiState(
    val currentYear: Int,
    val currentMonth: Int,
    val selectedDate: LocalDate,
    val coursesByDate: Map<LocalDate, List<CalendarCourse>> = emptyMap(),
    val selectedCourseId: Long? = null,
    val places: List<RecordPlace> = emptyList(),
    val photos: List<RecordPhoto> = emptyList(),
    val diaryContent: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

// 아래는 @Preview 전용 더미 데이터. 실제 실행 경로 데이터는 RecordRepositoryImpl 이 BE 응답으로 채운다.
private val previewPhotoUrls = listOf(
    "https://picsum.photos/seed/daytodo-record-1/400",
    "https://picsum.photos/seed/daytodo-record-2/400",
    "https://picsum.photos/seed/daytodo-record-3/400",
)

private fun previewPhotos(count: Int): List<RecordPhoto> = List(count) { index ->
    RecordPhoto(
        memoryPhotoId = index.toLong(),
        imageUrl = previewPhotoUrls[index % previewPhotoUrls.size],
        photoOrder = index,
    )
}

internal fun sampleRecordUiState(): RecordUiState = RecordUiState(
    currentYear = 2026,
    currentMonth = 5,
    selectedDate = LocalDate.of(2026, 5, 26),
    coursesByDate = mapOf(
        LocalDate.of(2026, 5, 5) to listOf(
            CalendarCourse(101L, "연남동 브런치 코스", ParticipantType.COUPLE, 2L),
        ),
        LocalDate.of(2026, 5, 12) to listOf(
            CalendarCourse(102L, "한강 피크닉 코스", ParticipantType.FRIEND, 3L),
            CalendarCourse(103L, "망원 카페 코스", ParticipantType.FRIEND, 3L),
        ),
        LocalDate.of(2026, 5, 19) to listOf(
            CalendarCourse(104L, "북촌 한옥 산책", ParticipantType.ALONE, 1L),
        ),
        LocalDate.of(2026, 5, 26) to listOf(
            CalendarCourse(105L, "성수 데이트 코스", ParticipantType.COUPLE, 2L),
            CalendarCourse(106L, "서울숲 나들이", ParticipantType.COUPLE, 2L),
        ),
    ),
    selectedCourseId = 105L,
    places = listOf(
        RecordPlace(coursePlaceId = 1L, placeId = 201L, placeName = "성수 브런치 카페", placeOrder = 1),
        RecordPlace(coursePlaceId = 2L, placeId = 202L, placeName = "서울숲", placeOrder = 2),
    ),
    photos = previewPhotos(5),
    diaryContent = "소품샵 어때 😊",
)
