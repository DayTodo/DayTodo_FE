package com.team_daytodo.daytodo.feature.calendar.state

import com.team_daytodo.daytodo.domain.calendar.model.CalendarCourse
import java.time.LocalDate

data class CalendarUiState(
    val currentYear: Int,
    val currentMonth: Int,
    val selectedDate: LocalDate,
    val coursesByDate: Map<LocalDate, List<CalendarCourse>> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
