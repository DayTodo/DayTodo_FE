package com.team_daytodo.daytodo.data.record

import com.team_daytodo.daytodo.domain.record.model.RecordCalendarData
import com.team_daytodo.daytodo.domain.record.model.RecordMemo
import com.team_daytodo.daytodo.domain.record.model.RecordVisitedCourse
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyRecordRepository @Inject constructor() : RecordRepository {
    // 코스가 있는 날짜만 5/5, 5/12, 5/19, 5/26 (5/19 는 코스는 있으나 사진 없음 케이스)
    private val courseDates: Set<LocalDate> = setOf(
        LocalDate.of(2026, 5, 5),
        LocalDate.of(2026, 5, 12),
        LocalDate.of(2026, 5, 19),
        LocalDate.of(2026, 5, 26),
    )

    private val photoCountsByDate: Map<LocalDate, Int> = mapOf(
        LocalDate.of(2026, 5, 5) to 4,
        LocalDate.of(2026, 5, 12) to 3,
        LocalDate.of(2026, 5, 19) to 0,
        LocalDate.of(2026, 5, 26) to 5,
    )

    private val coursesByDate: Map<LocalDate, List<RecordVisitedCourse>> = mapOf(
        LocalDate.of(2026, 5, 5) to listOf(
            RecordVisitedCourse(id = "0505-1", title = "연남동 브런치 코스"),
        ),
        LocalDate.of(2026, 5, 12) to listOf(
            RecordVisitedCourse(id = "0512-1", title = "한강 피크닉 코스"),
            RecordVisitedCourse(id = "0512-2", title = "망원 카페 코스"),
        ),
        LocalDate.of(2026, 5, 19) to listOf(
            RecordVisitedCourse(id = "0519-1", title = "북촌 한옥 산책"),
        ),
        LocalDate.of(2026, 5, 26) to listOf(
            RecordVisitedCourse(id = "0526-1", title = "성수 데이트 코스"),
            RecordVisitedCourse(id = "0526-2", title = "서울숲 나들이"),
            RecordVisitedCourse(id = "0526-3", title = "건대 맛집 투어"),
            RecordVisitedCourse(id = "0526-4", title = "잠실 야경 코스"),
        ),
    )

    // 기본 선택 날짜(5/26)의 첫 사진에만 댓글이 있고 나머지는 빈 상태로 시작한다.
    private val memosByPhotoId: MutableMap<String, MutableList<RecordMemo>> = mutableMapOf(
        "0526-0" to mutableListOf(
            RecordMemo(id = "m1", author = "나", content = "소품샵 어때 😊"),
            RecordMemo(id = "m2", author = "보라", content = "다음에 곰볼 갔다가 헤이티도 고고"),
            RecordMemo(id = "m3", author = "나", content = "고고고"),
        ),
    )

    private var nextMemoId = 0

    override suspend fun getRecordCalendar(): Result<RecordCalendarData> = runCatching {
        delay(RecordRequestDelayMillis)

        RecordCalendarData(
            courseDates = courseDates,
            photoCountsByDate = photoCountsByDate,
            coursesByDate = coursesByDate,
            memosByPhotoId = memosByPhotoId.mapValues { (_, memos) -> memos.toList() },
        )
    }

    override suspend fun addMemo(photoId: String, author: String, content: String): Result<RecordMemo> = runCatching {
        delay(RecordRequestDelayMillis)

        val memo = RecordMemo(id = "memo-${nextMemoId++}", author = author, content = content)
        memosByPhotoId.getOrPut(photoId) { mutableListOf() }.add(memo)
        memo
    }

    private companion object {
        const val RecordRequestDelayMillis = 300L
    }
}
