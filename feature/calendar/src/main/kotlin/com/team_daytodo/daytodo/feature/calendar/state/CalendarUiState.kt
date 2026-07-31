package com.team_daytodo.daytodo.feature.calendar.state

import java.time.LocalDate

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val coursesByDate: Map<LocalDate, String> = emptyMap(),
    val isLoading: Boolean = false,
)
