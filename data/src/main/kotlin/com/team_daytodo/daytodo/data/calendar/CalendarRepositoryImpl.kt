package com.team_daytodo.daytodo.data.calendar

import com.team_daytodo.daytodo.data.api.CalendarApi
import com.team_daytodo.daytodo.data.dto.calendar.toDomain
import com.team_daytodo.daytodo.domain.calendar.model.CalendarDate
import com.team_daytodo.daytodo.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarApi: CalendarApi,
) : CalendarRepository {
    override suspend fun getCalendar(year: Int, month: Int): Result<List<CalendarDate>> = runCatching {
        calendarApi.getCalendar(year, month).toDomain()
    }
}
