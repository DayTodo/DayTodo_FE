package com.team_daytodo.daytodo.feature.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.course.model.CourseDetail
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.PlaceRecommender
import com.team_daytodo.daytodo.domain.course.usecase.GetCourseDetailUseCase
import com.team_daytodo.daytodo.domain.course.usecase.MoveCoursePlaceUseCase
import com.team_daytodo.daytodo.domain.course.usecase.RecommendPlaceUseCase
import com.team_daytodo.daytodo.domain.course.usecase.RemoveCoursePlaceUseCase
import com.team_daytodo.daytodo.domain.course.usecase.SearchPlacesUseCase
import com.team_daytodo.daytodo.domain.course.usecase.ToggleCoursePlaceUseCase
import com.team_daytodo.daytodo.domain.course.usecase.TogglePlaceLikeUseCase
import com.team_daytodo.daytodo.feature.course.model.PlaceCourseMode
import com.team_daytodo.daytodo.feature.course.model.PlaceBottomSheetState
import com.team_daytodo.daytodo.feature.course.model.PlaceRecommendationEvent
import com.team_daytodo.daytodo.feature.course.model.PlaceRecommendationUiState
import com.team_daytodo.daytodo.feature.course.model.RecommenderFilter
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
class PlaceRecommendationViewModel @Inject constructor(
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val togglePlaceLikeUseCase: TogglePlaceLikeUseCase,
    private val recommendPlaceUseCase: RecommendPlaceUseCase,
    private val toggleCoursePlaceUseCase: ToggleCoursePlaceUseCase,
    private val removeCoursePlaceUseCase: RemoveCoursePlaceUseCase,
    private val moveCoursePlaceUseCase: MoveCoursePlaceUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaceRecommendationUiState(isLoading = true))
    val uiState: StateFlow<PlaceRecommendationUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<PlaceRecommendationEvent>()
    val event: SharedFlow<PlaceRecommendationEvent> = _event.asSharedFlow()

    private var loadedCourseId: String? = null
    private var coursePlaceMoveRequestVersion = 0

    fun loadCourse(courseId: String) {
        if (loadedCourseId == courseId && _uiState.value.course != null) return
        loadedCourseId = courseId
        loadCourseInternal(
            courseId = courseId,
            resetRecommender = true,
        )
    }

    fun refreshCourse(courseId: String) {
        loadedCourseId = courseId
        loadCourseInternal(
            courseId = courseId,
            resetRecommender = false,
        )
    }

    private fun loadCourseInternal(
        courseId: String,
        resetRecommender: Boolean,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getCourseDetailUseCase(courseId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            course = detail,
                            selectedRecommender = if (resetRecommender) {
                                RecommenderFilter.Ai
                            } else {
                                it.selectedRecommender
                            },
                            searchResults = it.searchResults.syncWith(detail),
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
                    _event.emit(PlaceRecommendationEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                searchPerformed = if (query.isBlank()) false else it.searchPerformed,
                searchResults = if (query.isBlank()) emptyList() else it.searchResults,
            )
        }
    }

    fun clearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                searchPerformed = false,
            )
        }
    }

    fun searchPlaces() {
        val courseId = loadedCourseId ?: return
        val query = _uiState.value.searchQuery
        viewModelScope.launch {
            searchPlacesUseCase(courseId, query)
                .onSuccess { results ->
                    _uiState.update {
                        it.copy(
                            searchResults = results,
                            searchPerformed = query.isNotBlank(),
                            selectedPlaceId = results.firstOrNull()?.place?.id ?: it.selectedPlaceId,
                        )
                    }
                }
                .onFailure { cause ->
                    _event.emit(PlaceRecommendationEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun selectMode(mode: PlaceCourseMode) {
        _uiState.update {
            it.copy(
                mode = mode,
                searchResults = emptyList(),
                searchPerformed = false,
                searchQuery = if (mode == PlaceCourseMode.Course) "" else it.searchQuery,
            )
        }
    }

    fun selectRecommender(filter: RecommenderFilter) {
        _uiState.update { it.copy(selectedRecommender = filter) }
    }

    fun selectPlace(placeId: String) {
        _uiState.update { it.copy(selectedPlaceId = placeId) }
    }

    fun toggleSheetExpanded() {
        _uiState.update {
            it.copy(
                sheetState = when (it.sheetState) {
                    PlaceBottomSheetState.Hidden -> PlaceBottomSheetState.Collapsed
                    PlaceBottomSheetState.Collapsed -> PlaceBottomSheetState.Expanded
                    PlaceBottomSheetState.Expanded -> PlaceBottomSheetState.Collapsed
                },
            )
        }
    }

    fun updateSheetState(sheetState: PlaceBottomSheetState) {
        _uiState.update { it.copy(sheetState = sheetState) }
    }

    fun toggleLike(placeId: String) {
        val courseId = loadedCourseId ?: return
        viewModelScope.launch {
            togglePlaceLikeUseCase(courseId, placeId)
                .onSuccess(::replaceCourseDetail)
                .onFailure { cause ->
                    _event.emit(PlaceRecommendationEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun recommendPlace(placeId: String) {
        val courseId = loadedCourseId ?: return
        viewModelScope.launch {
            recommendPlaceUseCase(courseId, placeId)
                .onSuccess { detail ->
                    replaceCourseDetail(detail)
                    _event.emit(PlaceRecommendationEvent.ShowMessage("추천 목록에 추가했어요."))
                }
                .onFailure { cause ->
                    _event.emit(PlaceRecommendationEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun toggleCoursePlace(placeId: String) {
        val courseId = loadedCourseId ?: return
        viewModelScope.launch {
            toggleCoursePlaceUseCase(courseId, placeId)
                .onSuccess { detail ->
                    replaceCourseDetail(detail)
                    _uiState.update { it.copy(mode = PlaceCourseMode.Course) }
                }
                .onFailure { cause ->
                    _event.emit(PlaceRecommendationEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun removeCoursePlace(placeId: String) {
        val courseId = loadedCourseId ?: return
        viewModelScope.launch {
            removeCoursePlaceUseCase(courseId, placeId)
                .onSuccess(::replaceCourseDetail)
                .onFailure { cause ->
                    _event.emit(PlaceRecommendationEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun moveCoursePlace(fromIndex: Int, toIndex: Int) {
        val courseId = loadedCourseId ?: return
        val previousDetail = _uiState.value.course ?: return
        val optimisticDetail = previousDetail.withMovedCoursePlace(fromIndex, toIndex) ?: return
        val requestVersion = ++coursePlaceMoveRequestVersion

        replaceCourseDetail(optimisticDetail)
        viewModelScope.launch {
            moveCoursePlaceUseCase(courseId, fromIndex, toIndex)
                .onSuccess { detail ->
                    if (requestVersion == coursePlaceMoveRequestVersion) {
                        replaceCourseDetail(detail)
                    }
                }
                .onFailure { cause ->
                    if (requestVersion == coursePlaceMoveRequestVersion) {
                        replaceCourseDetail(previousDetail)
                    }
                    _event.emit(PlaceRecommendationEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    private fun replaceCourseDetail(detail: CourseDetail) {
        _uiState.update {
            it.copy(
                course = detail,
                searchResults = it.searchResults.syncWith(detail),
            )
        }
    }
}

private fun CourseDetail.withMovedCoursePlace(
    fromIndex: Int,
    toIndex: Int,
): CourseDetail? {
    if (fromIndex !in coursePlaces.indices || toIndex !in coursePlaces.indices || fromIndex == toIndex) {
        return null
    }

    return copy(coursePlaces = coursePlaces.move(fromIndex, toIndex))
}

private fun List<CoursePlace>.move(
    fromIndex: Int,
    toIndex: Int,
): List<CoursePlace> =
    toMutableList().apply {
        val movedPlace = removeAt(fromIndex)
        add(toIndex, movedPlace)
    }

private fun List<CoursePlaceSearchResult>.syncWith(
    courseDetail: CourseDetail,
): List<CoursePlaceSearchResult> {
    val coursePlaceIds = courseDetail.coursePlaces.mapTo(mutableSetOf()) { it.id }
    val currentMemberRecommendedPlaceIds = courseDetail.recommendedPlaces
        .filter { recommendation ->
            val recommender = recommendation.recommender as? PlaceRecommender.Member
            recommender?.memberId == courseDetail.currentMemberId
        }
        .mapTo(mutableSetOf()) { it.place.id }

    return map { result ->
        result.copy(
            recommendedByCurrentMember = result.place.id in currentMemberRecommendedPlaceIds,
            isInCourse = result.place.id in coursePlaceIds,
        )
    }
}

private fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "요청을 처리하지 못했어요."
