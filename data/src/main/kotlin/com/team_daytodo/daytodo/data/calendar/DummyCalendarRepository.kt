package com.team_daytodo.daytodo.data.calendar

import com.team_daytodo.daytodo.domain.calendar.model.CalendarCourse
import com.team_daytodo.daytodo.domain.calendar.repository.CalendarRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.delay

class DummyCalendarRepository @Inject constructor() : CalendarRepository {
    override suspend fun getCourses(): Result<List<CalendarCourse>> = runCatching {
        delay(CalendarRequestDelayMillis)

        listOf(
            CalendarCourse(date = LocalDate.of(2026, 7, 29), title = "홍대거리 코스"),
            CalendarCourse(date = LocalDate.of(2026, 7, 16), title = "성수 데이트 코스"),
        )
    }

    private companion object {
        const val CalendarRequestDelayMillis = 300L
    }
}
