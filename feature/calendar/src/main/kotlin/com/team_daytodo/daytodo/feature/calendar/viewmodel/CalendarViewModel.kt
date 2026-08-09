package com.team_daytodo.daytodo.feature.calendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.calendar.usecase.GetCalendarCoursesUseCase
import com.team_daytodo.daytodo.feature.calendar.state.CalendarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarCoursesUseCase: GetCalendarCoursesUseCase,
) : ViewModel() {
    private val today = LocalDate.now()

    private val _uiState = MutableStateFlow(
        CalendarUiState(
            currentYear = today.year,
            currentMonth = today.monthValue,
            selectedDate = today,
        ),
    )
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private var lastRequestedMonth: YearMonth? = null

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        if (yearMonth == lastRequestedMonth) return
        lastRequestedMonth = yearMonth

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentYear = yearMonth.year,
                    currentMonth = yearMonth.monthValue,
                    isLoading = true,
                    errorMessage = null,
                )
            }
            getCalendarCoursesUseCase(yearMonth.year, yearMonth.monthValue)
                .onSuccess { calendarDates ->
                    _uiState.update {
                        it.copy(
                            coursesByDate = calendarDates.associate { it.date to it.courses },
                            isLoading = false,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                }
        }
    }
}
