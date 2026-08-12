package com.team_daytodo.daytodo.feature.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.usecase.GetUpcomingCoursesUseCase
import com.team_daytodo.daytodo.feature.course.model.CourseListEvent
import com.team_daytodo.daytodo.feature.course.model.CourseListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val getUpcomingCoursesUseCase: GetUpcomingCoursesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CourseListUiState(isLoading = true))
    val uiState: StateFlow<CourseListUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CourseListEvent>()
    val event: SharedFlow<CourseListEvent> = _event.asSharedFlow()

    init {
        loadCourses()
    }

    fun loadCourses(date: CourseDate? = _uiState.value.selectedDate) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getUpcomingCoursesUseCase(date)
                .onSuccess { courses ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            courses = courses,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = cause.userFacingMessage(),
                        )
                    }
                    _event.emit(CourseListEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun selectDate(date: CourseDate?) {
        _uiState.update { it.copy(selectedDate = date) }
        loadCourses(date)
    }
}

private fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "코스 정보를 불러오지 못했어요."
