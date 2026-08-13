package com.team_daytodo.daytodo.feature.today.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace
import com.team_daytodo.daytodo.domain.today.usecase.CompleteCourseUseCase
import com.team_daytodo.daytodo.domain.today.usecase.DeleteTodayPlaceUseCase
import com.team_daytodo.daytodo.domain.today.usecase.GetTodayCourseUseCase
import com.team_daytodo.daytodo.domain.today.usecase.ReorderTodayPlacesUseCase
import com.team_daytodo.daytodo.domain.today.usecase.SaveMemoryPhotosUseCase
import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace
import com.team_daytodo.daytodo.feature.today.model.TodayEvent
import com.team_daytodo.daytodo.feature.today.state.TodayUiState
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
class TodayViewModel @Inject constructor(
    private val getTodayCourseUseCase: GetTodayCourseUseCase,
    private val completeCourseUseCase: CompleteCourseUseCase,
    private val saveMemoryPhotosUseCase: SaveMemoryPhotosUseCase,
    private val reorderTodayPlacesUseCase: ReorderTodayPlacesUseCase,
    private val deleteTodayPlaceUseCase: DeleteTodayPlaceUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TodayUiState(isLoading = true))
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TodayEvent>()
    val event: SharedFlow<TodayEvent> = _event.asSharedFlow()

    // 순서 변경 API 실패 시 되돌릴, 드래그 시작 시점의 장소 목록
    private var placesBeforeDrag: List<CoursePlace>? = null

    init {
        loadTodayCourse()
    }

    fun retryLoad() {
        loadTodayCourse()
    }

    fun completeCourse() {
        val courseId = _uiState.value.courseId ?: return

        viewModelScope.launch {
            completeCourseUseCase(courseId)
                .onSuccess { loadTodayCourse() }
                .onFailure { cause ->
                    _uiState.update { it.copy(errorMessage = cause.message) }
                    _event.emit(TodayEvent.ShowMessage(cause.message ?: "코스를 종료하지 못했어요."))
                }
        }
    }

    // TODO: BE에 이미지 업로드 API가 아직 없어 로컬 갤러리 Uri 문자열을 그대로 imageUrls로 전달한다.
    // 서버에 실제로 접근 가능한 URL이 아니므로, 업로드 API가 생기면 업로드 후 받은 URL로 교체해야 한다.
    // 사진을 고르는 즉시(별도의 "저장" 버튼 없이) 업로드한다.
    fun addMemoryPhotos(uris: List<String>) {
        val courseId = _uiState.value.courseId ?: return
        if (uris.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(pendingMemoryPhotoUris = it.pendingMemoryPhotoUris + uris) }
            saveMemoryPhotosUseCase(courseId, uris)
                .onSuccess { saved ->
                    _uiState.update {
                        it.copy(
                            memoryPhotos = it.memoryPhotos + saved.photos,
                            pendingMemoryPhotoUris = it.pendingMemoryPhotoUris - uris.toSet(),
                        )
                    }
                    _event.emit(TodayEvent.MemoryPhotosSaved)
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(
                            pendingMemoryPhotoUris = it.pendingMemoryPhotoUris - uris.toSet(),
                            errorMessage = cause.message,
                        )
                    }
                    _event.emit(TodayEvent.ShowMessage(cause.message ?: "사진을 저장하지 못했어요."))
                }
        }
    }

    // 드래그가 시작될 때, 순서 변경 API 실패 시 되돌릴 스냅샷을 저장한다.
    fun onReorderDragStart() {
        placesBeforeDrag = _uiState.value.places
    }

    // 드래그가 끝나는 시점에 TodayScreen의 로컬 순서(orderedPlaceIds)를 서버에 반영하고,
    // 실패하면 드래그 시작 시점의 순서로 되돌린다.
    fun commitReorder(orderedPlaceIds: List<String>) {
        val courseId = _uiState.value.courseId ?: return
        val snapshot = placesBeforeDrag
        placesBeforeDrag = null

        val orderedCoursePlaceIds = orderedPlaceIds.mapNotNull { it.toLongOrNull() }

        viewModelScope.launch {
            reorderTodayPlacesUseCase(courseId, orderedCoursePlaceIds)
                .onSuccess { places -> _uiState.update { it.copy(places = places.toUiPlaces()) } }
                .onFailure { cause ->
                    if (snapshot != null) {
                        _uiState.update { it.copy(places = snapshot, errorMessage = cause.message) }
                    } else {
                        _uiState.update { it.copy(errorMessage = cause.message) }
                    }
                    _event.emit(TodayEvent.ShowMessage(cause.message ?: "장소 순서를 변경하지 못했어요."))
                }
        }
    }

    fun deleteCoursePlace(placeId: String) {
        val courseId = _uiState.value.courseId ?: return
        val coursePlaceId = placeId.toLongOrNull() ?: return

        viewModelScope.launch {
            deleteTodayPlaceUseCase(courseId, coursePlaceId)
                .onSuccess { places -> _uiState.update { it.copy(places = places.toUiPlaces()) } }
                .onFailure { cause ->
                    _uiState.update { it.copy(errorMessage = cause.message) }
                    _event.emit(TodayEvent.ShowMessage(cause.message ?: "장소를 삭제하지 못했어요."))
                }
        }
    }

    private fun loadTodayCourse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getTodayCourseUseCase()
                .onSuccess { course ->
                    _uiState.update {
                        it.copy(
                            hasCourse = course != null,
                            courseId = course?.courseId,
                            courseName = course?.courseName,
                            members = course?.members?.map { member ->
                                CourseMember(id = member.nickname, name = member.nickname)
                            }.orEmpty(),
                            places = course?.places?.toUiPlaces().orEmpty(),
                            isLoading = false,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                    _event.emit(TodayEvent.ShowMessage(cause.message ?: "투데이 코스를 불러오지 못했어요."))
                }
        }
    }

    private fun List<TodayCoursePlace>.toUiPlaces(): List<CoursePlace> = map { place ->
        CoursePlace(
            id = place.coursePlaceId.toString(),
            name = place.placeName,
            category = place.category,
        )
    }
}
