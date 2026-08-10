package com.team_daytodo.daytodo.domain.calendar.model

import java.time.LocalDate

data class CalendarDate(
    val date: LocalDate,
    val courses: List<CalendarCourse>,
)
