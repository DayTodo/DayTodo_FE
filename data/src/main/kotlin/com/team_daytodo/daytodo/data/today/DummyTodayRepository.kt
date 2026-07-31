package com.team_daytodo.daytodo.data.today

import com.team_daytodo.daytodo.domain.today.model.TodayCourse
import com.team_daytodo.daytodo.domain.today.model.TodayCourseMember
import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyTodayRepository @Inject constructor() : TodayRepository {
    override suspend fun getTodayCourse(): Result<TodayCourse> = runCatching {
        delay(TodayRequestDelayMillis)

        TodayCourse(
            hasCourse = true,
            members = listOf(
                TodayCourseMember(id = "1", name = "나"),
                TodayCourseMember(id = "2", name = "수아"),
                TodayCourseMember(id = "3", name = "민지"),
            ),
            places = listOf(
                TodayCoursePlace(id = "1", name = "성수 카페거리", category = "카페"),
                TodayCoursePlace(id = "2", name = "서울숲", category = "공원"),
                TodayCoursePlace(id = "3", name = "레스토랑 예약", category = "맛집"),
            ),
        )
    }

    private companion object {
        const val TodayRequestDelayMillis = 300L
    }
}
