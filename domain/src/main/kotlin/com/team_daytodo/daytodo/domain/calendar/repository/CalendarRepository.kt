package com.team_daytodo.daytodo.domain.calendar.repository

import com.team_daytodo.daytodo.domain.calendar.model.CalendarCourse

interface CalendarRepository {
    suspend fun getCourses(): Result<List<CalendarCourse>>
}
