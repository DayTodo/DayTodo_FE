package com.team_daytodo.daytodo.feature.record.model

import com.team_daytodo.daytodo.feature.record.R
import java.time.LocalDate
import java.time.YearMonth

data class RecordUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: YearMonth = YearMonth.now(),
    val courseDates: Set<LocalDate> = emptySet(),
    val photosByDate: Map<LocalDate, List<RecordPhoto>> = emptyMap(),
    val coursesByDate: Map<LocalDate, List<VisitedCourse>> = emptyMap(),
    val memosByPhotoId: Map<String, List<MemoEntry>> = emptyMap(),
    val isLoading: Boolean = false,
) {
    val selectedPhotos: List<RecordPhoto>
        get() = photosByDate[selectedDate].orEmpty()

    val selectedCourses: List<VisitedCourse>
        get() = coursesByDate[selectedDate].orEmpty()

    fun memosOf(photoId: String): List<MemoEntry> = memosByPhotoId[photoId].orEmpty()
}

// 아래는 @Preview 전용 더미 데이터. 실제 실행 경로의 더미 데이터는 DummyRecordRepository 에 있다.
private val previewCourseDates: Set<LocalDate> = setOf(
    LocalDate.of(2026, 5, 5),
    LocalDate.of(2026, 5, 12),
    LocalDate.of(2026, 5, 19),
    LocalDate.of(2026, 5, 26),
)

private val previewPhotoResources = listOf(
    R.drawable.dummypicture_1,
    R.drawable.dummypicture_2,
    R.drawable.dummypicture_3,
)

private fun previewPhotos(idPrefix: String, count: Int): List<RecordPhoto> = List(count) { index ->
    RecordPhoto(
        id = "$idPrefix-$index",
        imageRes = previewPhotoResources[index % previewPhotoResources.size],
    )
}

internal fun sampleRecordUiState(): RecordUiState = RecordUiState(
    selectedDate = LocalDate.of(2026, 5, 26),
    currentMonth = YearMonth.of(2026, 5),
    courseDates = previewCourseDates,
    photosByDate = mapOf(
        LocalDate.of(2026, 5, 5) to previewPhotos("0505", 4),
        LocalDate.of(2026, 5, 12) to previewPhotos("0512", 3),
        LocalDate.of(2026, 5, 19) to emptyList(),
        LocalDate.of(2026, 5, 26) to previewPhotos("0526", 5),
    ),
    coursesByDate = mapOf(
        LocalDate.of(2026, 5, 5) to listOf(
            VisitedCourse(id = "0505-1", title = "연남동 브런치 코스"),
        ),
        LocalDate.of(2026, 5, 12) to listOf(
            VisitedCourse(id = "0512-1", title = "한강 피크닉 코스"),
            VisitedCourse(id = "0512-2", title = "망원 카페 코스"),
        ),
        LocalDate.of(2026, 5, 19) to listOf(
            VisitedCourse(id = "0519-1", title = "북촌 한옥 산책"),
        ),
        LocalDate.of(2026, 5, 26) to listOf(
            VisitedCourse(id = "0526-1", title = "성수 데이트 코스"),
            VisitedCourse(id = "0526-2", title = "서울숲 나들이"),
            VisitedCourse(id = "0526-3", title = "건대 맛집 투어"),
            VisitedCourse(id = "0526-4", title = "잠실 야경 코스"),
        ),
    ),
    memosByPhotoId = mapOf(
        "0526-0" to listOf(
            MemoEntry(id = "m1", author = "나", content = "소품샵 어때 😊"),
            MemoEntry(id = "m2", author = "보라", content = "다음에 곰볼 갔다가 헤이티도 고고"),
            MemoEntry(id = "m3", author = "나", content = "고고고"),
        ),
    ),
)
