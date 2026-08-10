package com.team_daytodo.daytodo.domain.calendar.repository

import com.team_daytodo.daytodo.domain.calendar.model.CalendarDate

interface CalendarRepository {
    suspend fun getCalendar(year: Int, month: Int): Result<List<CalendarDate>>
}
