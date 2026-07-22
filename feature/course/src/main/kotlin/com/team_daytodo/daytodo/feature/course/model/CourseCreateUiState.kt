package com.team_daytodo.daytodo.feature.course.model

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup

data class CourseCreateUiState(
    val phase: CourseCreatePhase = CourseCreatePhase.Input,
    val currentStep: CourseCreateStep = CourseCreateStep.RoomName,
    val roomName: String = "",
    val selectedRegion: String = "",
    val selectedDate: CourseDate? = null,
    val minBudgetDigits: String = "",
    val maxBudgetDigits: String = "",
    val selectedRelationship: Relationship? = null,
    val regionOptions: List<CourseRegionGroup> = emptyList(),
    val isRegionLoading: Boolean = false,
    val isRegionLoadFailed: Boolean = false,
    val inviteLink: String = "",
    val completedRelationship: Relationship? = null,
) {
    val minBudget: Int?
        get() = minBudgetDigits.toIntOrNull()

    val maxBudget: Int?
        get() = maxBudgetDigits.toIntOrNull()

    val primaryButtonText: String
        get() = if (currentStep == CourseCreateStep.Relationship) {
            "방 만들기"
        } else {
            "다음 단계로"
        }

    val budgetErrorMessage: String?
        get() {
            val minBudget = minBudget ?: return null
            val maxBudget = maxBudget ?: return null
            return if (minBudget > maxBudget) {
                "최소 예산은 최대 예산보다 클 수 없어요."
            } else {
                null
            }
        }

    val isPrimaryButtonEnabled: Boolean
        get() = phase == CourseCreatePhase.Input && when (currentStep) {
            CourseCreateStep.RoomName -> roomName.isNotBlank()
            CourseCreateStep.Region -> selectedRegion.isNotBlank()
            CourseCreateStep.Date -> selectedDate != null
            CourseCreateStep.Budget -> {
                val minBudget = minBudget
                val maxBudget = maxBudget
                minBudget != null && maxBudget != null && minBudget <= maxBudget
            }
            CourseCreateStep.Relationship -> selectedRelationship != null
        }
}

sealed interface CourseCreatePhase {
    data object Input : CourseCreatePhase
    data object Loading : CourseCreatePhase
    data object Complete : CourseCreatePhase
}

enum class CourseCreateStep(val progressStep: Int) {
    RoomName(progressStep = 1),
    Region(progressStep = 2),
    Date(progressStep = 3),
    Budget(progressStep = 4),
    Relationship(progressStep = 5),
}

fun CourseCreateStep.next(): CourseCreateStep =
    when (this) {
        CourseCreateStep.RoomName -> CourseCreateStep.Region
        CourseCreateStep.Region -> CourseCreateStep.Date
        CourseCreateStep.Date -> CourseCreateStep.Budget
        CourseCreateStep.Budget -> CourseCreateStep.Relationship
        CourseCreateStep.Relationship -> CourseCreateStep.Relationship
    }

fun CourseCreateStep.previous(): CourseCreateStep =
    when (this) {
        CourseCreateStep.RoomName -> CourseCreateStep.RoomName
        CourseCreateStep.Region -> CourseCreateStep.RoomName
        CourseCreateStep.Date -> CourseCreateStep.Region
        CourseCreateStep.Budget -> CourseCreateStep.Date
        CourseCreateStep.Relationship -> CourseCreateStep.Budget
    }

sealed interface CourseCreateEvent {
    data class ShowMessage(val message: String) : CourseCreateEvent
}
