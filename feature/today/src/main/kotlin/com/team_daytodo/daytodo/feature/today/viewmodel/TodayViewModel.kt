package com.team_daytodo.daytodo.feature.today.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.today.usecase.GetTodayCourseUseCase
import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace
import com.team_daytodo.daytodo.feature.today.state.TodayUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getTodayCourseUseCase: GetTodayCourseUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        loadTodayCourse()
    }

    private fun loadTodayCourse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTodayCourseUseCase()
                .onSuccess { course ->
                    _uiState.update {
                        it.copy(
                            hasCourse = course.hasCourse,
                            members = course.members.map { member -> CourseMember(id = member.id, name = member.name) },
                            places = course.places.map { place ->
                                CoursePlace(id = place.id, name = place.name, category = place.category)
                            },
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}
