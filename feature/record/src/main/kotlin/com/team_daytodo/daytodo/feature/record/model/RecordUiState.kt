package com.team_daytodo.daytodo.feature.record.model

import com.team_daytodo.daytodo.domain.calendar.model.CalendarCourse
import com.team_daytodo.daytodo.domain.calendar.model.CourseStatus
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
    // 낙관적으로 즉시 반영되는 "저장됨" 표시 상태. 실제 삭제(DELETE) 호출에는 bookmarkId가
    // 필요해서 placeId만으로는 부족하므로, 확정된 매핑은 placeBookmarkIds에 별도로 둔다.
    val savedPlaceIds: Set<Long> = emptySet(),
    val placeBookmarkIds: Map<Long, Long> = emptyMap(),
    val pendingBookmarkPlaceIds: Set<Long> = emptySet(),
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
            CalendarCourse(101L, "연남동 브런치 코스", ParticipantType.COUPLE, 2L, CourseStatus.COMPLETED),
        ),
        LocalDate.of(2026, 5, 12) to listOf(
            CalendarCourse(102L, "한강 피크닉 코스", ParticipantType.FRIEND, 3L, CourseStatus.COMPLETED),
            CalendarCourse(103L, "망원 카페 코스", ParticipantType.FRIEND, 3L, CourseStatus.COMPLETED),
        ),
        LocalDate.of(2026, 5, 19) to listOf(
            CalendarCourse(104L, "북촌 한옥 산책", ParticipantType.ALONE, 1L, CourseStatus.COMPLETED),
        ),
        LocalDate.of(2026, 5, 26) to listOf(
            CalendarCourse(105L, "성수 데이트 코스", ParticipantType.COUPLE, 2L, CourseStatus.COMPLETED),
            CalendarCourse(106L, "서울숲 나들이", ParticipantType.COUPLE, 2L, CourseStatus.COMPLETED),
        ),
    ),
    selectedCourseId = 105L,
    places = listOf(
        RecordPlace(coursePlaceId = 1L, placeId = 201L, placeName = "성수 브런치 카페", placeOrder = 1),
        RecordPlace(coursePlaceId = 2L, placeId = 202L, placeName = "서울숲", placeOrder = 2),
    ),
    savedPlaceIds = setOf(201L),
    placeBookmarkIds = mapOf(201L to 9001L),
    photos = previewPhotos(5),
    diaryContent = "소품샵 어때 😊",
)
