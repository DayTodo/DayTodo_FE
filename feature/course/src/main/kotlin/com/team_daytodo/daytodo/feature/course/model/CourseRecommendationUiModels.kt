package com.team_daytodo.daytodo.feature.course.model

import com.team_daytodo.daytodo.domain.course.model.CourseComment
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseDetail
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceRecommendation
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.domain.course.model.PlaceRecommender
import java.text.DecimalFormat
import java.util.Calendar

data class CourseListUiState(
    val isLoading: Boolean = false,
    val courses: List<CourseSummary> = emptyList(),
    val selectedDate: CourseDate? = null,
    val errorMessage: String? = null,
) {
    val visibleCourses: List<CourseSummary>
        get() = selectedDate?.let { date ->
            courses.filter { it.date == date }
        } ?: courses.filter { it.date.isAfterToday() }
}

sealed interface CourseListEvent {
    data class ShowMessage(val message: String) : CourseListEvent
}

data class PlaceRecommendationUiState(
    val isLoading: Boolean = false,
    val course: CourseDetail? = null,
    val mode: PlaceCourseMode = PlaceCourseMode.Recommendation,
    val selectedRecommender: RecommenderFilter = RecommenderFilter.Ai,
    val searchQuery: String = "",
    val searchResults: List<CoursePlaceSearchResult> = emptyList(),
    val searchPerformed: Boolean = false,
    val selectedPlaceId: String? = null,
    val sheetState: PlaceBottomSheetState = PlaceBottomSheetState.Collapsed,
    val errorMessage: String? = null,
) {
    val recommenderFilters: List<RecommenderFilter>
        get() {
            val loadedCourse = course ?: return listOf(RecommenderFilter.All, RecommenderFilter.Ai)
            val memberFilters = loadedCourse.members.map { member ->
                RecommenderFilter.Member(
                    memberId = member.id,
                    name = if (member.id == loadedCourse.currentMemberId) "나" else member.name,
                )
            }
            return listOf(RecommenderFilter.Ai) + memberFilters + listOf(RecommenderFilter.All)
        }

    val visibleRecommendations: List<CoursePlaceRecommendation>
        get() {
            val recommendations = course?.recommendedPlaces.orEmpty()
            return when (selectedRecommender) {
                RecommenderFilter.All -> recommendations
                RecommenderFilter.Ai -> recommendations.filter {
                    it.recommender is PlaceRecommender.Ai
                }
                is RecommenderFilter.Member -> recommendations.filter { recommendation ->
                    val recommender = recommendation.recommender as? PlaceRecommender.Member
                    recommender?.memberId == selectedRecommender.memberId
                }
            }
        }

    val hasSearchOverlay: Boolean
        get() = searchQuery.isNotBlank() || searchPerformed

    val isSearchEmpty: Boolean
        get() = searchPerformed && searchResults.isEmpty()

    val displayedMapPlaces: List<MapMarkerPlace>
        get() {
            if (hasSearchOverlay && searchResults.isNotEmpty()) {
                return searchResults.mapIndexed { index, result ->
                    MapMarkerPlace(
                        place = result.place,
                        label = (index + 1).toString(),
                    )
                }
            }

            val loadedCourse = course ?: return emptyList()
            return when (mode) {
                PlaceCourseMode.Recommendation -> visibleRecommendations.map { recommendation ->
                    MapMarkerPlace(
                        place = recommendation.place,
                        label = recommendation.recommender.markerLabel(),
                    )
                }
                PlaceCourseMode.Course -> loadedCourse.coursePlaces.mapIndexed { index, place ->
                    MapMarkerPlace(
                        place = place,
                        label = (index + 1).toString(),
                    )
                }
            }
        }
}

enum class PlaceBottomSheetState {
    Hidden,
    Collapsed,
    Expanded,
}

data class MapMarkerPlace(
    val place: CoursePlace,
    val label: String,
)

enum class PlaceCourseMode {
    Recommendation,
    Course,
}

sealed interface RecommenderFilter {
    data object All : RecommenderFilter
    data object Ai : RecommenderFilter

    data class Member(
        val memberId: String,
        val name: String,
    ) : RecommenderFilter
}

sealed interface PlaceRecommendationEvent {
    data class ShowMessage(val message: String) : PlaceRecommendationEvent
}

data class CourseEditUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val course: CourseDetail? = null,
    val name: String = "",
    val selectedRegion: String = "",
    val selectedDate: CourseDate? = null,
    val minBudgetDigits: String = "",
    val maxBudgetDigits: String = "",
    val regionOptions: List<CourseRegionGroup> = emptyList(),
    val isRegionLoading: Boolean = false,
    val isRegionLoadFailed: Boolean = false,
    val errorMessage: String? = null,
) {
    val minBudget: Int?
        get() = minBudgetDigits.toIntOrNull()

    val maxBudget: Int?
        get() = maxBudgetDigits.toIntOrNull()

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

    val canSubmit: Boolean
        get() = !isSaving &&
            name.isNotBlank() &&
            regionOptions.containsRegion(selectedRegion) &&
            selectedRegion.isSupportedCourseRegion() &&
            selectedDate != null &&
            minBudget != null &&
            maxBudget != null &&
            budgetErrorMessage == null
}

sealed interface CourseEditEvent {
    data class ShowMessage(val message: String) : CourseEditEvent
    data object Saved : CourseEditEvent
}

data class PlaceCommentUiState(
    val isLoading: Boolean = false,
    val place: CoursePlace? = null,
    val comments: List<CourseComment> = emptyList(),
    val input: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val canSubmit: Boolean
        get() = input.isNotBlank() && !isSubmitting
}

sealed interface PlaceCommentEvent {
    data class ShowMessage(val message: String) : PlaceCommentEvent
}

fun CourseDate.displayKoreanDateWithWeekday(): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
    }
    val weekday = listOf("일", "월", "화", "수", "목", "금", "토")[
        calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
    ]

    return "$year ${month}월 ${day}일 ${weekday}요일"
}

fun CourseDate.displayEditText(): String = "$year.$month.$day"

private fun CourseDate.isAfterToday(): Boolean {
    val today = Calendar.getInstance()
    val currentYear = today.get(Calendar.YEAR)
    val currentMonth = today.get(Calendar.MONTH) + 1
    val currentDay = today.get(Calendar.DAY_OF_MONTH)

    return year > currentYear ||
        (year == currentYear && month > currentMonth) ||
        (year == currentYear && month == currentMonth && day > currentDay)
}

fun Int.formatWon(): String = DecimalFormat("#,###").format(this)

fun PlaceRecommender.displayName(): String =
    when (this) {
        PlaceRecommender.Ai -> "AI"
        is PlaceRecommender.Member -> name
    }

fun PlaceRecommender.markerLabel(): String =
    when (this) {
        PlaceRecommender.Ai -> "AI"
        is PlaceRecommender.Member -> name.take(2)
    }

fun RecommenderFilter.displayName(): String =
    when (this) {
        RecommenderFilter.All -> "전체"
        RecommenderFilter.Ai -> "AI"
        is RecommenderFilter.Member -> name
    }
