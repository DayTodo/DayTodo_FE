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

    val hasKnownSelectedRegion: Boolean
        get() = regionOptions.containsRegion(selectedRegion) && selectedRegion.isSupportedCourseRegion()

    val isPrimaryButtonEnabled: Boolean
        get() = phase == CourseCreatePhase.Input && when (currentStep) {
            CourseCreateStep.RoomName -> roomName.isNotBlank()
            CourseCreateStep.Region -> hasKnownSelectedRegion
            CourseCreateStep.Date -> selectedDate != null
            CourseCreateStep.Budget -> {
                val minBudget = minBudget
                val maxBudget = maxBudget
                minBudget != null && maxBudget != null && minBudget <= maxBudget
            }
            CourseCreateStep.Relationship -> selectedRelationship != null
        }
}

fun List<CourseRegionGroup>.containsRegion(region: String): Boolean {
    val normalizedRegion = region.trim()
    if (normalizedRegion.isBlank()) return false

    return any { group ->
        normalizedRegion == group.name || normalizedRegion in group.children
    }
}

fun String.isSupportedCourseRegion(): Boolean =
    trim() in SupportedCourseRegionNames

const val UnsupportedCourseRegionMessage = "해당 지역은 서비스하지 않아요."

private val SupportedCourseRegionNames = setOf(
    "서울 전체",
    "서울특별시",
    "종로구",
    "중구",
    "용산구",
    "성동구",
    "광진구",
    "동대문구",
    "중랑구",
    "성북구",
    "강북구",
    "도봉구",
    "노원구",
    "은평구",
    "서대문구",
    "마포구",
    "양천구",
    "강서구",
    "구로구",
    "금천구",
    "영등포구",
    "동작구",
    "관악구",
    "서초구",
    "강남구",
    "송파구",
    "강동구",
)

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
