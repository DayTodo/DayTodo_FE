package com.team_daytodo.daytodo.feature.record.model

import com.team_daytodo.daytodo.feature.record.R
import java.time.LocalDate
import java.time.YearMonth

data class RecordUiState(
    val selectedDate: LocalDate,
    val currentMonth: YearMonth,
    val courseDates: Set<LocalDate>,
    val photosByDate: Map<LocalDate, List<RecordPhoto>>,
    val coursesByDate: Map<LocalDate, List<VisitedCourse>>,
    val memosByPhotoId: Map<String, List<MemoEntry>>,
) {
    val selectedPhotos: List<RecordPhoto>
        get() = photosByDate[selectedDate].orEmpty()

    val selectedCourses: List<VisitedCourse>
        get() = coursesByDate[selectedDate].orEmpty()

    fun memosOf(photoId: String): List<MemoEntry> = memosByPhotoId[photoId].orEmpty()
}

// 더미 데이터: 5월 5/12/19/26 에만 코스 존재 (5/19 는 코스는 있으나 사진 없음 케이스)
private val dummyCourseDates: Set<LocalDate> = setOf(
    LocalDate.of(2026, 5, 5),
    LocalDate.of(2026, 5, 12),
    LocalDate.of(2026, 5, 19),
    LocalDate.of(2026, 5, 26),
)

// 미리보기용 더미 사진 3종을 순환시켜 사용한다.
private val dummyPhotoResources = listOf(
    R.drawable.dummypicture_1,
    R.drawable.dummypicture_2,
    R.drawable.dummypicture_3,
)

private fun dummyPhotos(idPrefix: String, count: Int): List<RecordPhoto> = List(count) { index ->
    RecordPhoto(
        id = "$idPrefix-$index",
        imageRes = dummyPhotoResources[index % dummyPhotoResources.size],
    )
}

internal fun sampleRecordUiState(): RecordUiState = RecordUiState(
    // 코스가 있는 날짜만 선택 가능하므로 기본 선택은 코스 있는 날짜(5/26)로 둔다.
    selectedDate = LocalDate.of(2026, 5, 26),
    currentMonth = YearMonth.of(2026, 5),
    courseDates = dummyCourseDates,
    photosByDate = mapOf(
        LocalDate.of(2026, 5, 5) to dummyPhotos("0505", 4),
        LocalDate.of(2026, 5, 12) to dummyPhotos("0512", 3),
        LocalDate.of(2026, 5, 19) to emptyList(), // 코스는 있으나 사진 없음
        LocalDate.of(2026, 5, 26) to dummyPhotos("0526", 5),
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
    // 기본 선택 날짜의 첫 사진에만 댓글이 있고 나머지는 빈 상태.
    memosByPhotoId = mapOf(
        "0526-0" to listOf(
            MemoEntry(id = "m1", author = "나", content = "소품샵 어때 😊"),
            MemoEntry(id = "m2", author = "보라", content = "다음에 곰볼 갔다가 헤이티도 고고"),
            MemoEntry(id = "m3", author = "나", content = "고고고"),
        ),
    ),
)
