package com.team_daytodo.daytodo.feature.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseUpdateRequest
import com.team_daytodo.daytodo.domain.course.usecase.GetCourseDetailUseCase
import com.team_daytodo.daytodo.domain.course.usecase.GetCourseRegionsUseCase
import com.team_daytodo.daytodo.domain.course.usecase.UpdateCourseSettingsUseCase
import com.team_daytodo.daytodo.feature.course.model.CourseEditEvent
import com.team_daytodo.daytodo.feature.course.model.CourseEditUiState
import com.team_daytodo.daytodo.feature.course.model.containsRegion
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
class CourseEditViewModel @Inject constructor(
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
    private val getCourseRegionsUseCase: GetCourseRegionsUseCase,
    private val updateCourseSettingsUseCase: UpdateCourseSettingsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CourseEditUiState(isLoading = true))
    val uiState: StateFlow<CourseEditUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CourseEditEvent>()
    val event: SharedFlow<CourseEditEvent> = _event.asSharedFlow()

    private var loadedCourseId: String? = null

    fun loadCourse(courseId: String) {
        if (loadedCourseId == courseId && _uiState.value.course != null) return
        loadedCourseId = courseId
        loadRegionOptions()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getCourseDetailUseCase(courseId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            course = detail,
                            name = detail.name,
                            selectedRegion = detail.region,
                            selectedDate = detail.date,
                            minBudgetDigits = detail.minBudget.toString(),
                            maxBudgetDigits = detail.maxBudget.toString(),
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
                    _event.emit(CourseEditEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun selectRegion(region: String) {
        val normalizedRegion = region.trim()
        _uiState.update { state ->
            if (state.regionOptions.containsRegion(normalizedRegion)) {
                state.copy(selectedRegion = normalizedRegion)
            } else {
                state.copy(selectedRegion = "")
            }
        }
    }

    fun selectDate(date: CourseDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun updateMinBudget(rawValue: String) {
        _uiState.update { it.copy(minBudgetDigits = rawValue.filter(Char::isDigit).take(MaxBudgetDigits)) }
    }

    fun updateMaxBudget(rawValue: String) {
        _uiState.update { it.copy(maxBudgetDigits = rawValue.filter(Char::isDigit).take(MaxBudgetDigits)) }
    }

    fun retryLoadRegionOptions() {
        loadRegionOptions()
    }

    fun save() {
        val state = _uiState.value
        val courseId = loadedCourseId ?: return
        val date = state.selectedDate
        val minBudget = state.minBudget
        val maxBudget = state.maxBudget

        if (date == null || minBudget == null || maxBudget == null || !state.canSubmit) {
            emitMessage(state.budgetErrorMessage ?: "입력값을 다시 확인해 주세요.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            updateCourseSettingsUseCase(
                CourseUpdateRequest(
                    courseId = courseId,
                    name = state.name,
                    region = state.selectedRegion,
                    date = date,
                    minBudget = minBudget,
                    maxBudget = maxBudget,
                ),
            ).onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        course = detail,
                    )
                }
                _event.emit(CourseEditEvent.Saved)
            }.onFailure { cause ->
                _uiState.update { it.copy(isSaving = false) }
                _event.emit(CourseEditEvent.ShowMessage(cause.userFacingMessage()))
            }
        }
    }

    private fun loadRegionOptions() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRegionLoading = true,
                    isRegionLoadFailed = false,
                )
            }
            getCourseRegionsUseCase()
                .onSuccess { regions ->
                    _uiState.update {
                        it.copy(
                            regionOptions = regions,
                            isRegionLoading = false,
                            isRegionLoadFailed = false,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(
                            isRegionLoading = false,
                            isRegionLoadFailed = true,
                        )
                    }
                    _event.emit(CourseEditEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    private fun emitMessage(message: String) {
        viewModelScope.launch {
            _event.emit(CourseEditEvent.ShowMessage(message))
        }
    }

    private companion object {
        const val MaxBudgetDigits = 9
    }
}

private fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "코스 설정을 처리하지 못했어요."
