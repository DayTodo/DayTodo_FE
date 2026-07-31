package com.team_daytodo.daytodo.domain.calendar.usecase

import com.team_daytodo.daytodo.domain.calendar.model.CalendarCourse
import com.team_daytodo.daytodo.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class GetCalendarCoursesUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
) {
    suspend operator fun invoke(): Result<List<CalendarCourse>> = calendarRepository.getCourses()
}
