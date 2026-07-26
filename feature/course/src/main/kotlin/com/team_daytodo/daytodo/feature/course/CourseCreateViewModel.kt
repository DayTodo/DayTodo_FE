package com.team_daytodo.daytodo.feature.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.usecase.CreateCourseRoomUseCase
import com.team_daytodo.daytodo.domain.course.usecase.GetCourseRegionsUseCase
import com.team_daytodo.daytodo.feature.course.model.CourseCreateEvent
import com.team_daytodo.daytodo.feature.course.model.CourseCreatePhase
import com.team_daytodo.daytodo.feature.course.model.CourseCreateStep
import com.team_daytodo.daytodo.feature.course.model.CourseCreateUiState
import com.team_daytodo.daytodo.feature.course.model.next
import com.team_daytodo.daytodo.feature.course.model.previous
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
class CourseCreateViewModel @Inject constructor(
    private val getCourseRegionsUseCase: GetCourseRegionsUseCase,
    private val createCourseRoomUseCase: CreateCourseRoomUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CourseCreateUiState())
    val uiState: StateFlow<CourseCreateUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CourseCreateEvent>()
    val event: SharedFlow<CourseCreateEvent> = _event.asSharedFlow()

    init {
        loadRegionOptions()
    }

    fun updateRoomName(roomName: String) {
        _uiState.update { it.copy(roomName = roomName) }
    }

    fun selectRegion(region: String) {
        _uiState.update { it.copy(selectedRegion = region.trim()) }
    }

    fun selectDate(date: CourseDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun updateMinBudget(rawValue: String) {
        _uiState.update {
            it.copy(minBudgetDigits = rawValue.filter(Char::isDigit).take(MaxBudgetDigits))
        }
    }

    fun updateMaxBudget(rawValue: String) {
        _uiState.update {
            it.copy(maxBudgetDigits = rawValue.filter(Char::isDigit).take(MaxBudgetDigits))
        }
    }

    fun selectRelationship(relationship: Relationship) {
        _uiState.update { it.copy(selectedRelationship = relationship) }
    }

    fun moveToNextStep() {
        val currentState = _uiState.value
        if (!currentState.isPrimaryButtonEnabled) {
            showMessage(currentState.invalidInputMessage())
            return
        }

        _uiState.update { it.copy(currentStep = it.currentStep.next()) }
    }

    fun moveToPreviousStep() {
        if (_uiState.value.phase == CourseCreatePhase.Loading) return

        _uiState.update { it.copy(currentStep = it.currentStep.previous()) }
    }

    fun retryLoadRegionOptions() {
        loadRegionOptions()
    }

    fun createCourseRoom() {
        val currentState = _uiState.value
        if (currentState.phase == CourseCreatePhase.Loading) return
        if (currentState.currentStep != CourseCreateStep.Relationship) return

        val request = currentState.toCreateRequestOrNull()
        val selectedRelationship = currentState.selectedRelationship
        if (request == null || selectedRelationship == null) {
            showMessage(currentState.invalidInputMessage())
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(phase = CourseCreatePhase.Loading) }
            createCourseRoomUseCase(request)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            phase = CourseCreatePhase.Complete,
                            inviteLink = result.inviteLink,
                            completedRelationship = selectedRelationship,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(phase = CourseCreatePhase.Input) }
                    _event.emit(
                        CourseCreateEvent.ShowMessage(
                            CourseCreateFailureMessage,
                        ),
                    )
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
                            regionOptions = emptyList(),
                            selectedRegion = "",
                            isRegionLoading = false,
                            isRegionLoadFailed = true,
                        )
                    }
                    _event.emit(
                        CourseCreateEvent.ShowMessage(
                            cause.userFacingMessage(defaultMessage = "지역 정보를 불러오지 못했어요. 잠시 후 다시 시도해 주세요."),
                        ),
                    )
                }
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _event.emit(CourseCreateEvent.ShowMessage(message))
        }
    }

    private companion object {
        const val MaxBudgetDigits = 9
        const val CourseCreateFailureMessage = "코스 방을 만들지 못했어요. 잠시 후 다시 시도해주세요."
    }
}

private fun CourseCreateUiState.toCreateRequestOrNull(): CourseCreateRequest? {
    val selectedDate = selectedDate ?: return null
    val minBudget = minBudget ?: return null
    val maxBudget = maxBudget ?: return null
    val selectedRelationship = selectedRelationship ?: return null

    return CourseCreateRequest(
        roomName = roomName.trim(),
        region = selectedRegion,
        date = selectedDate,
        minBudget = minBudget,
        maxBudget = maxBudget,
        relationship = selectedRelationship,
    )
}

private fun CourseCreateUiState.invalidInputMessage(): String =
    when (currentStep) {
        CourseCreateStep.RoomName -> "코스 방 이름을 입력해 주세요."
        CourseCreateStep.Region -> {
            if (isRegionLoadFailed) {
                "지역 정보를 다시 불러와 주세요."
            } else {
                "코스 지역을 선택해 주세요."
            }
        }
        CourseCreateStep.Date -> "코스를 즐길 날짜를 선택해 주세요."
        CourseCreateStep.Budget -> budgetErrorMessage ?: "예산을 입력해 주세요."
        CourseCreateStep.Relationship -> "함께할 관계를 선택해 주세요."
    }

private fun Throwable.userFacingMessage(defaultMessage: String): String =
    message?.takeIf(String::isNotBlank) ?: defaultMessage
